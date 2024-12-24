package com.traveladvisor.bookingserver.service.domain.usecase.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveladvisor.bookingserver.service.domain.exception.BookingApplicationServiceException;
import com.traveladvisor.bookingserver.service.domain.outbox.model.flight.FlightOutbox;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.FlightOutboxRepository;
import com.traveladvisor.common.domain.event.booking.BookingCancelledEventPayload;
import com.traveladvisor.common.domain.event.booking.HotelBookedEventPayload;
import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import com.traveladvisor.common.domain.vo.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.traveladvisor.common.domain.constant.booking.SagaActionConstants.BOOKING_SAGA_ACTION_NAME;
import static com.traveladvisor.common.domain.constant.common.DomainConstants.UTC;

@Slf4j
@RequiredArgsConstructor
@Component
public class FlightOutboxHelper {

    private final FlightOutboxRepository flightOutboxRepository;

    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(HotelBookedEventPayload hotelBookedEventPayload,
                     BookingStatus bookingStatus,
                     SagaActionStatus sagaActionStatus,
                     OutboxStatus outboxStatus,
                     UUID sagaActionId) {
        save(FlightOutbox.builder()
                .id(UUID.randomUUID())
                .sagaActionId(sagaActionId)
                .createdAt(hotelBookedEventPayload.getCreatedAt())
                .eventType(BOOKING_SAGA_ACTION_NAME)
                .eventPayload(serialize(hotelBookedEventPayload))
                .bookingStatus(bookingStatus)
                .sagaActionStatus(sagaActionStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(BookingCancelledEventPayload bookingCancelledEventPayload,
                     BookingStatus bookingStatus,
                     SagaActionStatus sagaActionStatus,
                     OutboxStatus outboxStatus,
                     UUID sagaActionId) {
        save(FlightOutbox.builder()
                .id(UUID.randomUUID())
                .sagaActionId(sagaActionId)
                .createdAt(bookingCancelledEventPayload.getCreatedAt())
                .eventType(BOOKING_SAGA_ACTION_NAME)
                .eventPayload(serialize(bookingCancelledEventPayload))
                .bookingStatus(bookingStatus)
                .sagaActionStatus(sagaActionStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(FlightOutbox flightOutbox) {
        FlightOutbox savedFlightOutbox = flightOutboxRepository.save(flightOutbox);

        if (savedFlightOutbox == null) {
            throw new BookingApplicationServiceException("FlightOutbox 저장에 실패했습니다. OutboxId: " +
                    flightOutbox.getId().toString());
        }

        log.info("FlightOutbox 저장을 완료했습니다. OutboxId: {}", flightOutbox.getId().toString());
    }

    private String serialize(HotelBookedEventPayload hotelBookedEventPayload) {
        try {
            return objectMapper.writeValueAsString(hotelBookedEventPayload);
        } catch (JsonProcessingException ex) {
            throw new BookingApplicationServiceException("HotelBookedEventPayload 직렬화에 실패했습니다. BookingId: " +
                    hotelBookedEventPayload.getBookingId(), ex);
        }
    }

    private String serialize(BookingCancelledEventPayload bookingCancelledEventPayload) {
        try {
            return objectMapper.writeValueAsString(bookingCancelledEventPayload);
        } catch (JsonProcessingException ex) {
            throw new BookingApplicationServiceException("bookingCancelledEventPayload 직렬화에 실패했습니다. BookingId: " +
                    bookingCancelledEventPayload.getBookingId(), ex);
        }
    }

    public Optional<FlightOutbox> findFlightOutboxBySagaIdAndSagaActionStatus(
            UUID sagaId, SagaActionStatus... sagaActionStatuses) {

        return flightOutboxRepository.findByEventTypeAndSagaActionIdAndSagaActionStatusIn(
                BOOKING_SAGA_ACTION_NAME, sagaId, sagaActionStatuses);
    }

    /**
     * FlightOutbox의 예약 상태와 Saga Action 상태를 변경합니다.
     *
     * @param flightOutbox
     * @param bookingStatus
     * @param sagaActionStatus
     */
    public void updateOutbox(
            FlightOutbox flightOutbox, BookingStatus bookingStatus, SagaActionStatus sagaActionStatus) {
        flightOutbox.setBookingStatus(bookingStatus);
        flightOutbox.setSagaActionStatus(sagaActionStatus);
        flightOutbox.setCompletedAt(ZonedDateTime.now(ZoneId.of(UTC)));
    }

}
