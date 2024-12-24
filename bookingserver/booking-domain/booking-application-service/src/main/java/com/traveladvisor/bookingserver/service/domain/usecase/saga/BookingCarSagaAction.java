package com.traveladvisor.bookingserver.service.domain.usecase.saga;

import com.traveladvisor.bookingserver.service.domain.BookingDomainService;
import com.traveladvisor.bookingserver.service.domain.dto.message.CarBookingResponse;
import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.bookingserver.service.domain.event.BookingCancelledEvent;
import com.traveladvisor.bookingserver.service.domain.exception.BookingNotFoundException;
import com.traveladvisor.bookingserver.service.domain.mapper.BookingMapper;
import com.traveladvisor.bookingserver.service.domain.outbox.model.car.CarOutbox;
import com.traveladvisor.bookingserver.service.domain.outbox.model.flight.FlightOutbox;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.BookingRepository;
import com.traveladvisor.bookingserver.service.domain.usecase.outbox.CarOutboxHelper;
import com.traveladvisor.bookingserver.service.domain.usecase.outbox.FlightOutboxHelper;
import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.saga.SagaAction;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import com.traveladvisor.common.domain.vo.BookingId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * 차량 서비스로부터 차량 예약 요청에 대한 처리 결과를 전달받아 그 다음 처리 혹은 직전 단계 보상 Saga Action을 수행합니다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class BookingCarSagaAction implements SagaAction<CarBookingResponse> {

    private final BookingDomainService bookingDomainService;
    private final BookingRepository bookingRepository;
    private final BookingSagaActionHelper bookingSagaActionHelper;
    private final BookingMapper bookingMapper;

    private final CarOutboxHelper carOutboxHelper;

    private final FlightOutboxHelper flightOutboxHelper;

    /**
     * 차량 예약 성공 시 예약서를 로컬 DB에 영속화 하고 호텔/항공권/차량 전체 예약을 마무리 합니다.
     */
    @Override
    @Transactional
    public void process(CarBookingResponse carBookingResponse) {
        Optional<CarOutbox> carOutboxOrEmpty =
                carOutboxHelper.findCarOutboxBySagaIdAndSagaStatus(
                        UUID.fromString(carBookingResponse.sagaActionId()), SagaActionStatus.PROCESSING); // 차량 예약 처리 전에 반드시 SagaActionStatus 가 PROCESSING 이어야 합니다.

        if (carOutboxOrEmpty.isEmpty()) {
            log.info("이미 예약 처리된 이벤트 입니다. SagaActionID {}", carBookingResponse.sagaActionId());
            return;
        }

        CarOutbox carOutbox = carOutboxOrEmpty.get();

        // 예약서를 조회합니다.
        Booking booking = findBookingByBookingId(UUID.fromString(carBookingResponse.bookingId()));

        // BookingStatus.CAR_BOOKED 이므로 SagaActionStatus.SUCCEEDED 로 설정합니다.
        SagaActionStatus sagaActionStatus = SagaActionStatus.SUCCEEDED;

        // Flight Outbox의 항공권 예약 상태와 Saga Action 상태를 업데이트 합니다.
        Optional<FlightOutbox> flightOutboxOrEmpty =
                flightOutboxHelper.findFlightOutboxBySagaIdAndSagaActionStatus(
                        UUID.fromString(carBookingResponse.sagaActionId()), SagaActionStatus.PROCESSING);
        if (flightOutboxOrEmpty.isEmpty()) {
            log.info("이미 예약 처리된 항공권 예약 이벤트 입니다. SagaActionID {}", carBookingResponse.sagaActionId());
            return;
        }
        FlightOutbox flightOutbox = flightOutboxOrEmpty.get();
        flightOutboxHelper.updateOutbox(flightOutbox, booking.getBookingStatus(), sagaActionStatus);

        // Car Outbox의 항공권 예약 상태와 Saga Action 상태를 업데이트 합니다.
        carOutboxHelper.updateOutbox(carOutbox, booking.getBookingStatus(), sagaActionStatus);
        carOutboxHelper.save(carOutbox);

        log.info("정상적으로 예약서의 전체 예약 상태를 완료({})로 변경했습니다. BookingId: {}",
                sagaActionStatus.toString(), booking.getId().getValue());
    }

    /**
     * 차량 예약 실패 시 예약서의 상태를 실패 상태로 전환합니다.
     *
     * @param carBookingResponse
     */
    @Override
    @Transactional
    public void compensate(CarBookingResponse carBookingResponse) {
        // 트랜잭션 보상 대상 차량 예약 Outbox을 조회합니다.
        Optional<CarOutbox> carOutboxOrEmpty =
                carOutboxHelper.findCarOutboxBySagaIdAndSagaStatus(
                        UUID.fromString(carBookingResponse.sagaActionId()), SagaActionStatus.PROCESSING);

        if (carOutboxOrEmpty.isEmpty()) {
            log.info("이미 예약 취소 처리된 이벤트 입니다. SagaActionID {}", carBookingResponse.sagaActionId());
            return;
        }

        CarOutbox carOutbox = carOutboxOrEmpty.get();

        Booking booking = findBookingByBookingId(UUID.fromString(carBookingResponse.bookingId()));

        // 예약서의 상태를 CANCELLING으로 변경합니다.
        BookingCancelledEvent bookingCancelledEvent = bookingDomainService
                .initializeBookingCancelling(booking, carBookingResponse.failureMessages());

        // 변경된 예약서를 저장합니다.
        bookingRepository.save(booking);

        // 예약서의 BookingStatus를 기반으로 다음 Saga Action 단계의 상태를 생성합니다.
        // BookingStatus.CANCELLING 이므로 -> SagaStatus.COMPENSATING 생성
        SagaActionStatus sagaActionStatus = bookingSagaActionHelper.toSagaActionStatus(booking.getBookingStatus());

        // Car Outbox의 차량 예약 상태와 Saga Action 상태를 업데이트 합니다.
        carOutboxHelper.updateOutbox(carOutbox, booking.getBookingStatus(), sagaActionStatus);
        // 변경된 Car Outbox 상태를 저장합니다.
        carOutboxHelper.save(carOutbox);

        // 항공권 예약 상태를 취소 상태로 변경하기 위해 Flight Outbox에 예약 취소 이벤트를 저장합니다.
        flightOutboxHelper.save(
                bookingMapper.toBookingCancelledEvent(bookingCancelledEvent),
                bookingCancelledEvent.getBooking().getBookingStatus(),
                sagaActionStatus,
                OutboxStatus.STARTED,
                UUID.fromString(carBookingResponse.sagaActionId()));

        log.info("예약서의 예약 상태가 CANCELLING으로 변경됐습니다. BookingId: {}",
                bookingCancelledEvent.getBooking().getId().getValue());
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
