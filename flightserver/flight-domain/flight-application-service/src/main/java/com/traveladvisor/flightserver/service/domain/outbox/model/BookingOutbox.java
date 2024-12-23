package com.traveladvisor.flightserver.service.domain.outbox.model;

import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import com.traveladvisor.common.domain.vo.FlightBookingApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Flight 서비스에서 Booking 서비스로 메시지를 보내기 위한 outbox 테이블입니다.
 */
@Getter @Builder
@AllArgsConstructor
public class BookingOutbox {

    private UUID id;
    private UUID sagaActionId;
    private String eventType;
    private String eventPayload;
    private FlightBookingApprovalStatus bookingStatus;
    @Setter
    private OutboxStatus outboxStatus;
    private SagaActionStatus sagaActionStatus;
    private ZonedDateTime completedAt;
    private ZonedDateTime createdAt;
    private int version;

}
