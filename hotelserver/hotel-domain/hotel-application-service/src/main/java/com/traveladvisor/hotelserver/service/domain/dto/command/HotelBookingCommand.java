package com.traveladvisor.hotelserver.service.domain.dto.command;

import com.traveladvisor.common.domain.vo.HotelBookingApprovalStatus;
import com.traveladvisor.common.domain.vo.HotelBookingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record HotelBookingCommand(
        @NotBlank String id,
        @NotBlank String sagaActionId,
        @NotBlank String bookingId,
        @NotBlank String hotelOfferId,
        @NotBlank String memberEmail,
        @NotNull BigDecimal totalPrice,
        @NotNull HotelBookingApprovalStatus bookingStatus,
        @NotNull ZonedDateTime createdAt

) {
}
