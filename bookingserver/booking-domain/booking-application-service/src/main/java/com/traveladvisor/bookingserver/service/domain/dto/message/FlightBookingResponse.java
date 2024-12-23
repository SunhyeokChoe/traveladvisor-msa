package com.traveladvisor.bookingserver.service.domain.dto.message;

import com.traveladvisor.common.domain.vo.FlightBookingApprovalStatus;

import java.time.ZonedDateTime;
import java.util.List;

public record FlightBookingResponse(
        String id,
        String sagaActionId,
        String bookingId,
        String flightOfferId,
        ZonedDateTime createdAt,
        FlightBookingApprovalStatus flightBookingApprovalStatus,
        List<String> failureMessages

) {

}
