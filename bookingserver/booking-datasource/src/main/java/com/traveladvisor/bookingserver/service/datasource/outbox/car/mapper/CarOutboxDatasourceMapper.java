package com.traveladvisor.bookingserver.service.datasource.outbox.car.mapper;

import com.traveladvisor.bookingserver.service.datasource.outbox.car.entity.CarOutboxEntity;
import com.traveladvisor.bookingserver.service.domain.outbox.model.car.CarOutbox;
import org.springframework.stereotype.Component;

@Component
public class CarOutboxDatasourceMapper {

    /**
     * CarOutbox -> CarOutboxEntity
     *
     * @param carOutbox 도메인 엔터티
     * @return CarOutboxEntity
     */
    public CarOutboxEntity toEntity(CarOutbox carOutbox) {
        return CarOutboxEntity.builder()
                .id(carOutbox.getId())
                .sagaActionId(carOutbox.getSagaActionId())
                .eventType(carOutbox.getEventType())
                .eventPayload(carOutbox.getEventPayload())
                .bookingStatus(carOutbox.getBookingStatus())
                .outboxStatus(carOutbox.getOutboxStatus())
                .sagaActionStatus(carOutbox.getSagaActionStatus())
                .completedAt(carOutbox.getCompletedAt())
                .version(carOutbox.getVersion())
                .build();
    }

    /**
     * CarOutboxEntity -> CarOutbox
     *
     * @param carOutboxEntity 엔터티
     * @return CarOutbox
     */
    public CarOutbox toDomain(CarOutboxEntity carOutboxEntity) {
        return CarOutbox.builder()
                .id(carOutboxEntity.getId())
                .sagaActionId(carOutboxEntity.getSagaActionId())
                .eventType(carOutboxEntity.getEventType())
                .eventPayload(carOutboxEntity.getEventPayload())
                .bookingStatus(carOutboxEntity.getBookingStatus())
                .outboxStatus(carOutboxEntity.getOutboxStatus())
                .sagaActionStatus(carOutboxEntity.getSagaActionStatus())
                .completedAt(carOutboxEntity.getCompletedAt())
                .createdAt(carOutboxEntity.getCreatedAt())
                .version(carOutboxEntity.getVersion())
                .build();
    }

}
