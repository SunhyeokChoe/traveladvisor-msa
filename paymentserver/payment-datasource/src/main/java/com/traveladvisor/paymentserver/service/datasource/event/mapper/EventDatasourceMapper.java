package com.traveladvisor.paymentserver.service.datasource.event.mapper;

import com.traveladvisor.paymentserver.service.datasource.event.entity.EventEntity;
import com.traveladvisor.paymentserver.service.domain.entity.Event;
import com.traveladvisor.paymentserver.service.domain.vo.EventId;
import org.springframework.stereotype.Component;

@Component
public class EventDatasourceMapper {

    /**
     * EventEntity -> Event
     *
     * @param eventEntity 이벤트 엔티티
     * @return Event 도메인 객체
     */
    public Event toDomain(EventEntity eventEntity) {
        return Event.builder()
                .eventId(new EventId(eventEntity.getId()))
                .eventName(eventEntity.getEventName())
                .eventDescription(eventEntity.getEventDescription())
                .actionType(eventEntity.getActionType())
                .rewardType(eventEntity.getRewardType())
                .rewardValue(eventEntity.getRewardValue())
                .startDate(eventEntity.getStartDate())
                .endDate(eventEntity.getEndDate())
                .status(eventEntity.getStatus())
                .build();
    }

    /**
     * Event -> EventEntity
     *
     * @param event 이벤트 도메인 객체
     * @return EventEntity 객체
     */
    public EventEntity toEntity(Event event) {
        return EventEntity.builder()
                .id(event.getId() != null ? event.getId().getValue() : null)
                .eventName(event.getEventName())
                .eventDescription(event.getEventDescription())
                .actionType(event.getActionType())
                .rewardType(event.getRewardType())
                .rewardValue(event.getRewardValue())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .status(event.getStatus())
                .build();
    }

}
