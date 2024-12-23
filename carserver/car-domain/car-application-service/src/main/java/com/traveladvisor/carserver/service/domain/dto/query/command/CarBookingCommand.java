package com.traveladvisor.carserver.service.domain.dto.query.command;

import com.traveladvisor.common.domain.vo.CarBookingApprovalStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record CarBookingCommand(
        @NotBlank String id,
        @NotBlank String sagaActionId,
        @NotBlank String bookingId,
        @NotBlank String carOfferId,
        @NotBlank String memberEmail,
        @NotNull BigDecimal totalPrice,
        @NotNull CarBookingApprovalStatus bookingStatus,
        @NotNull ZonedDateTime createdAt

) {
}
