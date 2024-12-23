package com.traveladvisor.common.domain.event.flight;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * flight.booking_outbox의 payload에 들어갈 클래스입니다. payload는 JSONB 타입으로 저장되어야 하므로 직렬화, 역직렬화가 가능해야 합니다.
 */
@Getter @Builder
@AllArgsConstructor
public class FlightBookingCompletedEventPayload {

    @JsonProperty
    private String bookingId;
    @JsonProperty
    private String flightOfferId;
    @JsonProperty
    private String flightBookingApprovalStatus;
    @JsonProperty
    private List<String> failureMessages;
    @JsonProperty
    private ZonedDateTime createdAt;

}
