package com.traveladvisor.bookingserver.service.domain.usecase.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveladvisor.bookingserver.service.domain.exception.BookingApplicationServiceException;
import com.traveladvisor.bookingserver.service.domain.outbox.model.hotel.HotelOutbox;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.HotelOutboxRepository;
import com.traveladvisor.common.domain.event.booking.BookingCancelledEventPayload;
import com.traveladvisor.common.domain.event.booking.BookingCreatedEventPayload;
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
public class HotelOutboxHelper {

    private final HotelOutboxRepository hotelOutboxRepository;

    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(BookingCreatedEventPayload bookingCreatedEventPayload,
                     BookingStatus bookingStatus,
                     SagaActionStatus sagaActionStatus,
                     OutboxStatus outboxStatus,
                     UUID sagaActionId) {
        save(HotelOutbox.builder()
                .id(UUID.randomUUID())
                .sagaActionId(sagaActionId)
                .createdAt(bookingCreatedEventPayload.getCreatedAt())
                .eventType(BOOKING_SAGA_ACTION_NAME)
                .eventPayload(serialize(bookingCreatedEventPayload))
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
        save(HotelOutbox.builder()
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
    public void save(HotelOutbox hotelOutbox) {
        HotelOutbox savedHotelOutbox = hotelOutboxRepository.save(hotelOutbox);

        if (savedHotelOutbox == null) {
            throw new BookingApplicationServiceException("HotelOutbox 저장에 실패했습니다. OutboxId: " +
                    hotelOutbox.getId().toString());
        }

        log.info("HotelOutbox를 저장 완료했습니다. OutboxId: {}", hotelOutbox.getId().toString());
    }

    private String serialize(BookingCreatedEventPayload bookingCreatedEventPayload) {
        try {
            return objectMapper.writeValueAsString(bookingCreatedEventPayload);
        } catch (JsonProcessingException ex) {
            throw new BookingApplicationServiceException("BookingCreatedEventPayload 직렬화에 실패했습니다. BookingId: " +
                    bookingCreatedEventPayload.getBookingId(), ex);
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

    public Optional<HotelOutbox> findHotelOutboxBySagaIdAndSagaStatus(UUID sagaId,
                                                                      SagaActionStatus... sagaActionStatuses) {

        return hotelOutboxRepository.findByEventTypeAndSagaActionIdAndSagaActionStatusIn(
                BOOKING_SAGA_ACTION_NAME, sagaId, sagaActionStatuses);
    }

    /**
     * HotelOutbox의 예약 상태와 Saga Action 상태를 변경합니다.
     *
     * @param hotelOutbox
     * @param bookingStatus
     * @param sagaActionStatus
     */
    public void updateOutbox(
            HotelOutbox hotelOutbox, BookingStatus bookingStatus, SagaActionStatus sagaActionStatus) {
        hotelOutbox.setBookingStatus(bookingStatus);
        hotelOutbox.setSagaActionStatus(sagaActionStatus);
        hotelOutbox.setCompletedAt(ZonedDateTime.now(ZoneId.of(UTC)));
    }

}
