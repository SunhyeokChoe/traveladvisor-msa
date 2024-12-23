package com.traveladvisor.flightserver.service.domain.dto.command;

import com.traveladvisor.common.domain.vo.FlightBookingApprovalStatus;
import com.traveladvisor.common.domain.vo.FlightBookingStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record FlightBookingCommand(
        @NotBlank String id,
        @NotBlank String sagaActionId,
        @NotBlank String bookingId,
        @NotBlank String flightOfferId,
        @NotBlank String memberEmail,
        @NotNull BigDecimal totalPrice,
        @NotNull FlightBookingApprovalStatus bookingStatus,
        @NotNull ZonedDateTime createdAt

) {
}
