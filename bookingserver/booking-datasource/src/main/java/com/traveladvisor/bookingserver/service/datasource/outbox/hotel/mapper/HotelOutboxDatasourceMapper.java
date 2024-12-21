package com.traveladvisor.bookingserver.service.datasource.outbox.hotel.mapper;

import com.traveladvisor.bookingserver.service.datasource.outbox.hotel.entity.HotelOutboxEntity;
import com.traveladvisor.bookingserver.service.domain.outbox.model.hotel.HotelOutbox;
import org.springframework.stereotype.Component;

@Component
public class HotelOutboxDatasourceMapper {

    /**
     * HotelOutbox -> HotelOutboxEntity
     *
     * @param hotelOutbox 도메인 엔터티
     * @return HotelOutboxEntity
     */
    public HotelOutboxEntity toEntity(HotelOutbox hotelOutbox) {
        return HotelOutboxEntity.builder()
                .id(hotelOutbox.getId())
                .sagaActionId(hotelOutbox.getSagaActionId())
                .eventType(hotelOutbox.getEventType())
                .eventPayload(hotelOutbox.getEventPayload())
                .bookingStatus(hotelOutbox.getBookingStatus())
                .outboxStatus(hotelOutbox.getOutboxStatus())
                .sagaActionStatus(hotelOutbox.getSagaActionStatus())
                .completedAt(hotelOutbox.getCompletedAt())
                .version(hotelOutbox.getVersion())
                .build();
    }

    /**
     * HotelOutboxEntity -> HotelOutbox
     *
     * @param hotelOutboxEntity 엔터티
     * @return HotelOutbox
     */
    public HotelOutbox toDomain(HotelOutboxEntity hotelOutboxEntity) {
        return HotelOutbox.builder()
                .id(hotelOutboxEntity.getId())
                .sagaActionId(hotelOutboxEntity.getSagaActionId())
                .eventType(hotelOutboxEntity.getEventType())
                .eventPayload(hotelOutboxEntity.getEventPayload())
                .bookingStatus(hotelOutboxEntity.getBookingStatus())
                .outboxStatus(hotelOutboxEntity.getOutboxStatus())
                .sagaActionStatus(hotelOutboxEntity.getSagaActionStatus())
                .completedAt(hotelOutboxEntity.getCompletedAt())
                .createdAt(hotelOutboxEntity.getCreatedAt())
                .version(hotelOutboxEntity.getVersion())
                .build();
    }

}
