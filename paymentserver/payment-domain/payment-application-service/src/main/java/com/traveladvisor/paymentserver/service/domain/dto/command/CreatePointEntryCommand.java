package com.traveladvisor.paymentserver.service.domain.dto.command;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreatePointEntryCommand(
        @NotNull UUID memberId

) {}
