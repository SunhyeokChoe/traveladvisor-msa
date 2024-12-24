package com.traveladvisor.bookingserver.service.domain.dto.message;

import com.traveladvisor.common.domain.vo.FlightBookingApprovalStatus;

import java.time.ZonedDateTime;
import java.util.List;

public record CarBookingResponse(
        String id,
        String sagaActionId,
        String bookingId,
        String carOfferId,
        ZonedDateTime createdAt,
        FlightBookingApprovalStatus carBookingApprovalStatus,
        List<String> failureMessages

) {

}
