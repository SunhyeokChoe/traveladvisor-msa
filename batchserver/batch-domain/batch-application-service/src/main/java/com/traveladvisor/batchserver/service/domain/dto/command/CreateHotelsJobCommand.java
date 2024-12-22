package com.traveladvisor.batchserver.service.domain.dto.command;

import jakarta.validation.constraints.NotBlank;

public record CreateHotelsJobCommand(
        @NotBlank String clientId,
        @NotBlank String clientSecret,
        @NotBlank String grantType

) {}
