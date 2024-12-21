package com.traveladvisor.bookingserver.service.domain.dto.query;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public record QueryFlightOffersResponse(
        Long id,
        String carrierCode,
        String operatingCarrierCode,
        String flightNumber,
        String aircraftCode,
        String departureIata,
        ZonedDateTime departureAt,
        String arrivalIata,
        ZonedDateTime arrivalAt,
        String duration,
        Boolean blacklistedInEu,
        Integer numberOfStops,
        BigDecimal price

) {
}
