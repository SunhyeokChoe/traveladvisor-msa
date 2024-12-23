package com.traveladvisor.bookingserver.service.domain.usecase.saga;

import com.traveladvisor.bookingserver.service.domain.BookingDomainService;
import com.traveladvisor.bookingserver.service.domain.dto.message.HotelBookingResponse;
import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.bookingserver.service.domain.event.HotelBookedEvent;
import com.traveladvisor.bookingserver.service.domain.exception.BookingNotFoundException;
import com.traveladvisor.bookingserver.service.domain.mapper.BookingMapper;
import com.traveladvisor.bookingserver.service.domain.outbox.model.hotel.HotelOutbox;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.BookingRepository;
import com.traveladvisor.bookingserver.service.domain.usecase.outbox.FlightOutboxHelper;
import com.traveladvisor.bookingserver.service.domain.usecase.outbox.HotelOutboxHelper;
import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.saga.SagaAction;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.traveladvisor.common.domain.constant.common.DomainConstants.UTC;

/**
 * 호텔 서비스로부터 호텔 예약 요청에 대한 처리 결과를 전달받아 그 다음 처리 혹은 직전 단계 보상 Saga Action을 수행합니다.
 * <p>
 * 예약서의 예약 상태를 호텔 예약 상태로 변경 완료 한 후 저장까지 완료하면
 * Saga Action의 다음 단계인 항공권 예약을 위해 process 메서드에서 booking.flight_outbox 테이블에 데이터를 저장합니다.
 * 그럼 Debezium CDC 측에서 이를 인지하고 카프카 토픽 debezium.booking.flight_outbox으로 메시지를 발행할 것이고,
 * Flight 서비스의 카프카 리스너가 이를 처리할 것입니다.
 * compensate 메서드의 경우 outbox 테이블에 아무런 영속화 작업도 하지 않습니다. 호텔 예약 이전 단계가 존재하지 않으므로
 * 그저 예약서의 상태를 실패로 변경하고 Saga Action을 중단하기만 하면 됩니다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BookingHotelSagaAction implements SagaAction<HotelBookingResponse> {

    private final BookingDomainService bookingDomainService;
    private final BookingRepository bookingRepository;
    private final BookingSagaActionHelper bookingSagaActionHelper;
    private final BookingMapper bookingMapper;

    private final HotelOutboxHelper hotelOutboxHelper;

    private final FlightOutboxHelper flightOutboxHelper;

    /**
     * 호텔 예약 성공 시 항공권 예약을 요청합니다.
     */
    @Override
    @Transactional
    public void process(HotelBookingResponse hotelBookingResponse) {
        Optional<HotelOutbox> hotelOutboxOrEmpty =
                hotelOutboxHelper.getPaymentOutboxMessageBySagaIdAndSagaStatus(
                        UUID.fromString(hotelBookingResponse.sagaActionId()), SagaActionStatus.STARTED);

        if (hotelOutboxOrEmpty.isEmpty()) {
            log.info("이미 처리된 이벤트 입니다. SagaActionID {}", hotelBookingResponse.sagaActionId());
            return;
        }

        HotelOutbox hotelOutbox = hotelOutboxOrEmpty.get();

        // 예약서의 호텔 객실 예약 상태를 HOTEL_BOOKED로 변경하고 HotelBookedEvent를 초기화 합니다.
        HotelBookedEvent hotelBookedEvent = completeHotelBooking(hotelBookingResponse);

        // BookingStatus를 기반으로 다음 Saga Action 단계의 상태를 생성합니다.
        // BookingStatus.HOTEL_BOOKED 이므로 -> SagaStatus.PROCESSING 생성
        SagaActionStatus sagaActionStatus = bookingSagaActionHelper
                .toSagaActionStatus(hotelBookedEvent.getBooking().getBookingStatus());

        // 호텔 Outbox의 호텔 예약 상태와 Saga Action 상태를 업데이트 합니다.
        updateHotelOutbox(hotelOutbox, hotelBookedEvent.getBooking().getBookingStatus(), sagaActionStatus);

        // booking.hotel_outbox 테이블의 Saga Action 상태를 업데이트 합니다.
        // HotelOutbox를 조회한 시점의 version과 DB에 저장되어 있는 레코드의 version이 서로 다른 경우
        // 다른 트랜잭션에서 이 값을 업데이트 한 것이므로 업데이트 하지 않습니다.
        // 참고로 낙관적 락의 단점은 롤백입니다. 충돌이 잦으면 롤백도 잦아지게 되어
        // DB와 애플리케이션 처리량에 좋지 않은 영향을 끼칠 수 있습니다.
        // 반면 충돌이 적을 것으로 예상된다면 적용해도 좋습니다. 업데이트 시 행을 잠그는 비관적 락보다 성능이 우수하기 때문입니다.
        hotelOutboxHelper.save(hotelOutbox);

        // Saga Action의 다음 단계인 항공권 예약 요청을 위해 booking.flight_outbox 테이블에
        // HotelBookedEvent 이벤트를 저장합니다. (OutboxStatus.STARTED, SagaActionStatus.PROCESSING)
        flightOutboxHelper.save(
                bookingMapper.toHotelBookedEventPayload(hotelBookedEvent),
                hotelBookedEvent.getBooking().getBookingStatus(),
                sagaActionStatus,
                OutboxStatus.STARTED,
                UUID.fromString(hotelBookingResponse.sagaActionId()));

        log.info("정상적으로 호텔 객실 예약 상태를 호텔 예약 완료({})로 처리합니다. BookingId: {}",
                hotelBookedEvent.getBooking().getBookingStatus().name(),
                hotelBookedEvent.getBooking().getId().getValue());
    }

    /**
     * HotelOutbox의 예약 상태와 Saga Action 상태를 변경합니다.
     *
     * @param hotelOutbox
     * @param bookingStatus
     * @param sagaActionStatus
     */
    private static void updateHotelOutbox(
            HotelOutbox hotelOutbox, BookingStatus bookingStatus, SagaActionStatus sagaActionStatus) {
        hotelOutbox.setBookingStatus(bookingStatus);
        hotelOutbox.setSagaActionStatus(sagaActionStatus);
        hotelOutbox.setCompletedAt(ZonedDateTime.now(ZoneId.of(UTC)));
    }

    /**
     * 호텔 예약 실패 시 예약서의 상태를 실패 상태로 전환합니다.
     *
     * @param hotelBookingResponse
     */
    @Override
    @Transactional
    public void compensate(HotelBookingResponse hotelBookingResponse) {

    }

    /**
     * 예약서 상태를 호텔 예약 완료로 변경하고 저장합니다.
     *
     * @param hotelBookingResponse
     * @return
     */
    private HotelBookedEvent completeHotelBooking(HotelBookingResponse hotelBookingResponse) {
        // 전달받은 예약서 ID로 호텔 예약 완료 처리할 대상을 DB에서 검색 조회합니다.
        Booking booking = findBookingByBookingId(UUID.fromString(hotelBookingResponse.bookingId()));

        // 예약서의 호텔 예약을 완료 처리합니다.
        HotelBookedEvent hotelBookedEvent = bookingDomainService.markAsHotelBooked(booking);

        // 주문서 현재 상태 저장 (주문 상태: HOTEL_BOOKED)
        bookingRepository.save(booking);

        return hotelBookedEvent;
    }

    /**
     * 예약서를 조회합니다.
     *
     * @param bookingId
     * @return
     */
    private Booking findBookingByBookingId(UUID bookingId) {
        return bookingRepository.findById(new BookingId(bookingId))
                .orElseThrow(() -> {
                    log.error("예약서가 존재하지 않습니다. BookingId: {}", bookingId);
                    return new BookingNotFoundException("예약서가 존재하지 않습니다. BookingId: " + bookingId.toString());
                });
    }

}
