package com.traveladvisor.paymentserver.service.domain.dto.command;

import com.traveladvisor.common.domain.vo.EventActionType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateRewardCommand(
        @NotNull UUID memberId,
        @NotNull EventActionType eventActionType

) {}
