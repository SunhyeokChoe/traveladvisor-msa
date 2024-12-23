package com.traveladvisor.flightserver.service.domain.usecase.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveladvisor.common.domain.event.flight.FlightBookingCompletedEventPayload;
import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.vo.FlightBookingApprovalStatus;
import com.traveladvisor.flightserver.service.domain.exception.FlightApplicationServiceException;
import com.traveladvisor.flightserver.service.domain.outbox.model.BookingOutbox;
import com.traveladvisor.flightserver.service.domain.port.output.repository.BookingOutboxRepository;
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
public class BookingOutboxHelper {

    private final BookingOutboxRepository bookingOutboxRepository;
    private final ObjectMapper objectMapper;

    public Optional<BookingOutbox> findCompletedBookingOutboxMessage(
            UUID sagaActionId,
            FlightBookingApprovalStatus flightBookingApprovalStatus) {

        return bookingOutboxRepository.findByEventTypeAndSagaActionIdAndBookingStatusAndOutboxStatus(
                BOOKING_SAGA_ACTION_NAME,
                sagaActionId,
                flightBookingApprovalStatus,
                OutboxStatus.COMPLETED);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveBookingOutbox(
            FlightBookingCompletedEventPayload flightBookingCompletedEventPayload,
            FlightBookingApprovalStatus flightBookingApprovalStatus,
            OutboxStatus outboxStatus,
            UUID sagaActionId) {
        save(BookingOutbox.builder()
                .id(UUID.randomUUID())
                .sagaActionId(sagaActionId)
                .createdAt(flightBookingCompletedEventPayload.getCreatedAt())
                .completedAt(ZonedDateTime.now(ZoneId.of(UTC)))
                .eventType(BOOKING_SAGA_ACTION_NAME)
                .eventPayload(serialize(flightBookingCompletedEventPayload))
                .bookingStatus(flightBookingApprovalStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    private String serialize(FlightBookingCompletedEventPayload flightBookingCompletedEventPayload) {
        try {
            return objectMapper.writeValueAsString(flightBookingCompletedEventPayload);
        } catch (JsonProcessingException ex) {
            log.error("flightBookingCompletedEventPayload 직렬화에 실패했습니다.", ex);
            throw new FlightApplicationServiceException("flightBookingCompletedEventPayload 직렬화에 실패했습니다.", ex);
        }
    }

    private void save(BookingOutbox bookingOutbox) {
        BookingOutbox savedBookingOutbox = bookingOutboxRepository.save(bookingOutbox);
        if (savedBookingOutbox == null) {
            log.error("BookingOutbox 저장에 실패했습니다.");
            throw new FlightApplicationServiceException("BookingOutbox 저장에 실패했습니다.");
        }

        log.info("BookingOutbox 저장을 완료했습니다. BookingOutboxId: {}", bookingOutbox.getId().toString());
    }

}
