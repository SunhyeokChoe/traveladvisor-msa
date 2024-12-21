package com.traveladvisor.carserver.service.domain.dto.query;

import java.math.BigDecimal;

public record QueryCarOffersResponse(
        Long id,
        String vehicleCode,
        String category,
        String description,
        String imageUrl,
        String baggages,
        String seats,
        BigDecimal price

) {
}
