package com.traveladvisor.bookingserver.service.domain.usecase.saga;

import com.traveladvisor.bookingserver.service.domain.BookingDomainService;
import com.traveladvisor.bookingserver.service.domain.dto.message.FlightBookingResponse;
import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.bookingserver.service.domain.event.FlightBookedEvent;
import com.traveladvisor.bookingserver.service.domain.exception.BookingNotFoundException;
import com.traveladvisor.bookingserver.service.domain.mapper.BookingMapper;
import com.traveladvisor.bookingserver.service.domain.outbox.model.flight.FlightOutbox;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.BookingRepository;
import com.traveladvisor.bookingserver.service.domain.usecase.outbox.CarOutboxHelper;
import com.traveladvisor.bookingserver.service.domain.usecase.outbox.FlightOutboxHelper;
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
 * 항공권 서비스로부터 항공권 예약 요청에 대한 처리 결과를 전달받아 그 다음 처리 혹은 직전 단계 보상 Saga Action을 수행합니다.
 *
 * 예약서의 예약 상태를 호텔 예약 상태로 변경 완료 한 후 저장까지 완료하면
 * Saga Action의 다음 단계인 차량 예약을 위해 process 메서드에서 booking.car_outbox 테이블에 데이터를 저장합니다.
 * 그럼 Debezium CDC 측에서 이를 인지하고 카프카 토픽 debezium.booking.car_outbox으로 메시지를 발행할 것이고,
 * Car 서비스의 카프카 리스너가 이를 처리할 것입니다.
 * compensate 메서드의 경우 outbox 테이블에 아무런 영속화 작업도 하지 않습니다. 호텔 예약 이전 단계가 존재하지 않으므로
 * 그저 예약서의 상태를 실패로 변경하고 Saga Action을 중단하기만 하면 됩니다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BookingFlightSagaAction implements SagaAction<FlightBookingResponse> {

    private final BookingDomainService bookingDomainService;
    private final BookingRepository bookingRepository;
    private final BookingSagaActionHelper bookingSagaActionHelper;
    private final BookingMapper bookingMapper;

    private final FlightOutboxHelper flightOutboxHelper;

    private final CarOutboxHelper carOutboxHelper;

    /**
     * 항공권 예약 성공 시 예약서를 로컬 DB에 영속화 하고 차량 예약을 요청합니다.
     */
    @Override
    @Transactional
    public void process(FlightBookingResponse flightBookingResponse) {
        Optional<FlightOutbox> flightOutboxOrEmpty =
                flightOutboxHelper.findFlightOutboxBySagaIdAndSagaStatus(
                        UUID.fromString(flightBookingResponse.sagaActionId()), SagaActionStatus.PROCESSING); // 항공권 예약 처리 전에 반드시 SagaActionStatus 가 PROCESSING 이어야 합니다.

        if (flightOutboxOrEmpty.isEmpty()) {
            log.info("이미 처리된 이벤트 입니다. SagaActionID {}", flightBookingResponse.sagaActionId());
            return;
        }

        FlightOutbox flightOutbox = flightOutboxOrEmpty.get();

        // 예약서의 항공권 예약 상태를 FLIGHT_BOOKED로 변경하고 FlightBookedEvent를 초기화 합니다.
        FlightBookedEvent flightBookedEvent = completeFlightBooking(flightBookingResponse);

        // BookingStatus를 기반으로 다음 Saga Action 단계의 상태를 생성합니다.
        // BookingStatus.FLIGHT_BOOKED 이므로 -> SagaStatus.PROCESSING 생성
        SagaActionStatus sagaActionStatus = bookingSagaActionHelper
                .toSagaActionStatus(flightBookedEvent.getBooking().getBookingStatus());

        // 항공권 Outbox의 항공권 예약 상태와 Saga Action 상태를 업데이트 합니다.
        updateHotelOutbox(flightOutbox, flightBookedEvent.getBooking().getBookingStatus(), sagaActionStatus);

        flightOutboxHelper.save(flightOutbox);

        // Saga Action의 다음 단계인 차량 예약 요청을 위해 booking.car_outbox 테이블에
        // FlightBookedEvent 이벤트를 저장합니다. (OutboxStatus.STARTED, SagaActionStatus.PROCESSING)
        carOutboxHelper.save(
                bookingMapper.toFlightBookedEventPayload(flightBookedEvent),
                flightBookedEvent.getBooking().getBookingStatus(),
                sagaActionStatus,
                OutboxStatus.STARTED,
                UUID.fromString(flightBookingResponse.sagaActionId()));

        log.info("정상적으로 예약서의 예약 상태를 항공권 예약 완료({})로 변경했습니다. BookingId: {}",
                flightBookedEvent.getBooking().getBookingStatus().name(),
                flightBookedEvent.getBooking().getId().getValue());
    }

    /**
     * FlightOutbox의 예약 상태와 Saga Action 상태를 변경합니다.
     *
     * @param flightOutbox
     * @param bookingStatus
     * @param sagaActionStatus
     */
    private static void updateHotelOutbox(
            FlightOutbox flightOutbox, BookingStatus bookingStatus, SagaActionStatus sagaActionStatus) {
        flightOutbox.setBookingStatus(bookingStatus);
        flightOutbox.setSagaActionStatus(sagaActionStatus);
        flightOutbox.setCompletedAt(ZonedDateTime.now(ZoneId.of(UTC)));
    }

    /**
     * 항공권 예약 실패 시 예약서의 상태를 실패 상태로 전환합니다.
     *
     * @param hotelBookingResponse
     */
    @Override
    @Transactional
    public void compensate(FlightBookingResponse hotelBookingResponse) {

    }

    /**
     * 예약서 상태를 항공권 예약 완료로 변경하고 저장합니다.
     *
     * @param flightBookingResponse
     * @return
     */
    private FlightBookedEvent completeFlightBooking(FlightBookingResponse flightBookingResponse) {
        // 전달받은 예약서 ID로 항공권 예약 완료 처리할 대상을 DB에서 검색 조회합니다.
        Booking booking = findBookingByBookingId(UUID.fromString(flightBookingResponse.bookingId()));

        // 예약서의 호텔 예약을 완료 처리합니다.
        FlightBookedEvent flightBookedEvent = bookingDomainService.markAsFlightBooked(booking);

        // 호텔 예약 상태를 저장합니다. (예약 상태: FLIGHT_BOOKED)
        bookingRepository.save(booking);

        return flightBookedEvent;
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
