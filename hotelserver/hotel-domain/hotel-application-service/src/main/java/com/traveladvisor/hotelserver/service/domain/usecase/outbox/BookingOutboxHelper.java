package com.traveladvisor.hotelserver.service.domain.usecase.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveladvisor.common.domain.event.hotel.HotelBookingCompletedEventPayload;
import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.vo.HotelBookingApprovalStatus;
import com.traveladvisor.hotelserver.service.domain.exception.HotelApplicationServiceException;
import com.traveladvisor.hotelserver.service.domain.outbox.model.BookingOutbox;
import com.traveladvisor.hotelserver.service.domain.port.output.repository.BookingOutboxRepository;
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
            HotelBookingApprovalStatus hotelBookingApprovalStatus) {

        return bookingOutboxRepository.findByEventTypeAndSagaActionIdAndBookingStatusAndOutboxStatus(
                BOOKING_SAGA_ACTION_NAME,
                sagaActionId,
                hotelBookingApprovalStatus,
                OutboxStatus.COMPLETED);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveBookingOutbox(
            HotelBookingCompletedEventPayload hotelBookingCompletedEventPayload,
            HotelBookingApprovalStatus hotelBookingApprovalStatus,
            OutboxStatus outboxStatus,
            UUID sagaActionId) {
        save(BookingOutbox.builder()
                .id(UUID.randomUUID())
                .sagaActionId(sagaActionId)
                .createdAt(hotelBookingCompletedEventPayload.getCreatedAt())
                .completedAt(ZonedDateTime.now(ZoneId.of(UTC)))
                .eventType(BOOKING_SAGA_ACTION_NAME)
                .eventPayload(serialize(hotelBookingCompletedEventPayload))
                .bookingStatus(hotelBookingApprovalStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    private String serialize(HotelBookingCompletedEventPayload hotelBookingCompletedEventPayload) {
        try {
            return objectMapper.writeValueAsString(hotelBookingCompletedEventPayload);
        } catch (JsonProcessingException ex) {
            log.error("hotelBookingApprovedEventPayload 직렬화에 실패했습니다.", ex);
            throw new HotelApplicationServiceException("hotelBookingApprovedEventPayload 직렬화에 실패했습니다.", ex);
        }
    }

    private void save(BookingOutbox bookingOutbox) {
        BookingOutbox savedBookingOutbox = bookingOutboxRepository.save(bookingOutbox);
        if (savedBookingOutbox == null) {
            log.error("BookingOutbox 저장에 실패했습니다.");
            throw new HotelApplicationServiceException("BookingOutbox 저장에 실패했습니다.");
        }

        log.info("BookingOutbox 저장을 완료했습니다. BookingOutboxId: {}", bookingOutbox.getId().toString());
    }

}
