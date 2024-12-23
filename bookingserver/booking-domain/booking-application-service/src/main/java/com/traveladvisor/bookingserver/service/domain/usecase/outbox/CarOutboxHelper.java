package com.traveladvisor.bookingserver.service.domain.usecase.outbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveladvisor.bookingserver.service.domain.exception.BookingApplicationServiceException;
import com.traveladvisor.bookingserver.service.domain.outbox.model.car.CarOutbox;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.CarOutboxRepository;
import com.traveladvisor.common.domain.event.booking.FlightBookedEventPayload;
import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import com.traveladvisor.common.domain.vo.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.traveladvisor.common.domain.constant.booking.SagaActionConstants.BOOKING_SAGA_ACTION_NAME;

@Slf4j
@RequiredArgsConstructor
@Component
public class CarOutboxHelper {

    private final CarOutboxRepository carOutboxRepository;

    private final ObjectMapper objectMapper;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(FlightBookedEventPayload flightBookedEventPayload,
                     BookingStatus bookingStatus,
                     SagaActionStatus sagaActionStatus,
                     OutboxStatus outboxStatus,
                     UUID sagaActionId) {
        save(CarOutbox.builder()
                .id(UUID.randomUUID())
                .sagaActionId(sagaActionId)
                .createdAt(flightBookedEventPayload.getCreatedAt())
                .eventType(BOOKING_SAGA_ACTION_NAME)
                .eventPayload(serialize(flightBookedEventPayload))
                .bookingStatus(bookingStatus)
                .sagaActionStatus(sagaActionStatus)
                .outboxStatus(outboxStatus)
                .build());
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(CarOutbox carOutbox) {
        CarOutbox savedCarOutbox = carOutboxRepository.save(carOutbox);

        if (savedCarOutbox == null) {
            throw new BookingApplicationServiceException("CarOutbox 저장에 실패했습니다. OutboxId: " +
                    carOutbox.getId().toString());
        }

        log.info("CarOutbox 저장을 완료했습니다. OutboxId: {}", carOutbox.getId().toString());
    }

    private String serialize(FlightBookedEventPayload hotelBookedEventPayload) {
        try {
            return objectMapper.writeValueAsString(hotelBookedEventPayload);
        } catch (JsonProcessingException ex) {
            throw new BookingApplicationServiceException("HotelBookedEventPayload 직렬화에 실패했습니다. BookingId: " +
                    hotelBookedEventPayload.getBookingId(), ex);
        }
    }

    public Optional<CarOutbox> findCarOutboxBySagaIdAndSagaStatus(
            UUID sagaId, SagaActionStatus... sagaActionStatuses) {

        return carOutboxRepository.findByEventTypeAndSagaActionIdAndSagaActionStatusIn(
                BOOKING_SAGA_ACTION_NAME, sagaId, sagaActionStatuses);
    }

}
