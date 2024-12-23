package com.traveladvisor.carserver.service.domain.usecase.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveladvisor.carserver.service.domain.exception.CarApplicationServiceException;
import com.traveladvisor.carserver.service.domain.outbox.model.BookingOutbox;
import com.traveladvisor.carserver.service.domain.port.output.repository.BookingOutboxRepository;
import com.traveladvisor.common.domain.event.car.CarBookingCompletedEventPayload;
import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.vo.CarBookingApprovalStatus;
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
            CarBookingApprovalStatus carBookingApprovalStatus) {

        return bookingOutboxRepository.findByEventTypeAndSagaActionIdAndBookingStatusAndOutboxStatus(
                BOOKING_SAGA_ACTION_NAME,
                sagaActionId,
                carBookingApprovalStatus,
                OutboxStatus.COMPLETED);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveBookingOutbox(
            CarBookingCompletedEventPayload carBookingCompletedEventPayload,
            CarBookingApprovalStatus carBookingApprovalStatus,
            OutboxStatus outboxStatus,
            UUID sagaActionId) {
        save(BookingOutbox.builder()
                .id(UUID.randomUUID())
                .sagaActionId(sagaActionId)
                .createdAt(carBookingCompletedEventPayload.getCreatedAt())
                .completedAt(ZonedDateTime.now(ZoneId.of(UTC)))
                .eventType(BOOKING_SAGA_ACTION_NAME)
                .eventPayload(serialize(carBookingCompletedEventPayload))
                .bookingStatus(carBookingApprovalStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    private String serialize(CarBookingCompletedEventPayload carBookingCompletedEventPayload) {
        try {
            return objectMapper.writeValueAsString(carBookingCompletedEventPayload);
        } catch (JsonProcessingException ex) {
            log.error("carBookingCompletedEventPayload 직렬화에 실패했습니다.", ex);
            throw new CarApplicationServiceException("carBookingCompletedEventPayload 직렬화에 실패했습니다.", ex);
        }
    }

    private void save(BookingOutbox bookingOutbox) {
        BookingOutbox savedBookingOutbox = bookingOutboxRepository.save(bookingOutbox);
        if (savedBookingOutbox == null) {
            log.error("BookingOutbox 저장에 실패했습니다.");
            throw new CarApplicationServiceException("BookingOutbox 저장에 실패했습니다.");
        }

        log.info("BookingOutbox 저장을 완료했습니다. BookingOutboxId: {}", bookingOutbox.getId().toString());
    }

}
