package com.traveladvisor.bookingserver.service.domain.dto.command;

import com.traveladvisor.common.domain.vo.BookingStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * 예약 요청 응답 DTO 입니다.
 */
public record CreateBookingResponse(
        @NotNull
        UUID bookingTraceId,
        @NotNull
        BookingStatus bookingStatus,
        @NotNull
        String message

) {
}
