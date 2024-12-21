package com.traveladvisor.bookingserver.service.domain.dto.query;

import com.traveladvisor.common.domain.vo.BookingStatus;

import java.util.List;
import java.util.UUID;

public record QueryBookingResponse(
        UUID traceId,
        BookingStatus bookingStatus,
        List<String>failureMessages

) {
}
