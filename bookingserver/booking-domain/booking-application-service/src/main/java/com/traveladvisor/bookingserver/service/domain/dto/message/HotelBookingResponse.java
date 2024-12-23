package com.traveladvisor.bookingserver.service.domain.dto.message;

import com.traveladvisor.common.domain.vo.HotelBookingApprovalStatus;

import java.time.ZonedDateTime;
import java.util.List;

public record HotelBookingResponse(
        String id,
        String sagaActionId,
        String bookingId,
        String hotelOfferId,
        ZonedDateTime createdAt,
        HotelBookingApprovalStatus hotelBookingApprovalStatus,
        List<String> failureMessages

) {

}
