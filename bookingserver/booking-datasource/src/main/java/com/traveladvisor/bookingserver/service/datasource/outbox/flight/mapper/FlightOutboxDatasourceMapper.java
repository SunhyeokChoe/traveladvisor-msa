package com.traveladvisor.bookingserver.service.datasource.outbox.flight.mapper;

import com.traveladvisor.bookingserver.service.datasource.outbox.flight.entity.FlightOutboxEntity;
import com.traveladvisor.bookingserver.service.domain.outbox.model.flight.FlightOutbox;
import org.springframework.stereotype.Component;

@Component
public class FlightOutboxDatasourceMapper {

    /**
     * FlightOutbox -> FlightOutboxEntity
     *
     * @param flightOutbox 도메인 엔터티
     * @return FlightOutboxEntity
     */
    public FlightOutboxEntity toEntity(FlightOutbox flightOutbox) {
        if (flightOutbox == null) {
            return null;
        }

        return FlightOutboxEntity.builder()
                .id(flightOutbox.getId())
                .sagaActionId(flightOutbox.getSagaActionId())
                .eventType(flightOutbox.getEventType())
                .eventPayload(flightOutbox.getEventPayload())
                .bookingStatus(flightOutbox.getBookingStatus())
                .outboxStatus(flightOutbox.getOutboxStatus())
                .sagaActionStatus(flightOutbox.getSagaActionStatus())
                .completedAt(flightOutbox.getCompletedAt())
                .version(flightOutbox.getVersion())
                .build();
    }

    /**
     * FlightOutboxEntity -> FlightOutbox
     *
     * @param flightOutboxEntity 엔터티
     * @return FlightOutbox
     */
    public FlightOutbox toDomain(FlightOutboxEntity flightOutboxEntity) {
        if (flightOutboxEntity == null) {
            return null;
        }

        return FlightOutbox.builder()
                .id(flightOutboxEntity.getId())
                .sagaActionId(flightOutboxEntity.getSagaActionId())
                .eventType(flightOutboxEntity.getEventType())
                .eventPayload(flightOutboxEntity.getEventPayload())
                .bookingStatus(flightOutboxEntity.getBookingStatus())
                .outboxStatus(flightOutboxEntity.getOutboxStatus())
                .sagaActionStatus(flightOutboxEntity.getSagaActionStatus())
                .completedAt(flightOutboxEntity.getCompletedAt())
                .createdAt(flightOutboxEntity.getCreatedAt())
                .version(flightOutboxEntity.getVersion())
                .build();
    }

}
