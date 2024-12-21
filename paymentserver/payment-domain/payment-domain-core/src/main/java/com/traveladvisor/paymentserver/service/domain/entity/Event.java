package com.traveladvisor.paymentserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.AggregateRoot;
import com.traveladvisor.common.domain.vo.EventActionType;
import com.traveladvisor.paymentserver.service.domain.vo.EventId;
import com.traveladvisor.paymentserver.service.domain.vo.EventRewardType;
import com.traveladvisor.paymentserver.service.domain.vo.EventStatus;

import java.time.ZonedDateTime;

public class Event extends AggregateRoot<EventId> {

    private final String eventName;
    private final String eventDescription;
    private final EventActionType actionType;
    private final EventRewardType rewardType;
    private final String rewardValue;
    private final ZonedDateTime startDate;
    private final ZonedDateTime endDate;

    private EventStatus status;

    /**
     * 이벤트 상태를 업데이트합니다.
     *
     * @param status 새로운 이벤트 상태
     */
    public void updateStatus(EventStatus status) {
        this.status = status;
    }

    private Event(Builder builder) {
        super.setId(builder.eventId);
        eventName = builder.eventName;
        eventDescription = builder.eventDescription;
        actionType = builder.actionType;
        rewardType = builder.rewardType;
        rewardValue = builder.rewardValue;
        startDate = builder.startDate;
        endDate = builder.endDate;
        status = builder.status;
    }

    // BEGIN: Getter
    public String getEventName() {
        return eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public EventActionType getActionType() {
        return actionType;
    }

    public EventRewardType getRewardType() {
        return rewardType;
    }

    public String getRewardValue() {
        return rewardValue;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public EventStatus getStatus() {
        return status;
    }
    // END: Getter

    // BEGIN: Builder
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private EventId eventId;
        private String eventName;
        private String eventDescription;
        private EventActionType actionType;
        private EventRewardType rewardType;
        private String rewardValue;
        private ZonedDateTime startDate;
        private ZonedDateTime endDate;
        private EventStatus status;

        private Builder() {
        }

        public Builder eventId(EventId val) {
            eventId = val;
            return this;
        }

        public Builder eventName(String val) {
            eventName = val;
            return this;
        }

        public Builder eventDescription(String val) {
            eventDescription = val;
            return this;
        }

        public Builder actionType(EventActionType val) {
            actionType = val;
            return this;
        }

        public Builder rewardType(EventRewardType val) {
            rewardType = val;
            return this;
        }

        public Builder rewardValue(String val) {
            rewardValue = val;
            return this;
        }

        public Builder startDate(ZonedDateTime val) {
            startDate = val;
            return this;
        }

        public Builder endDate(ZonedDateTime val) {
            endDate = val;
            return this;
        }

        public Builder status(EventStatus val) {
            status = val;
            return this;
        }

        public Event build() {
            return new Event(this);
        }
    }
    // END: Builder

}
