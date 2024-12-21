package com.traveladvisor.hotelserver.service.domain.dto.query;

import java.time.LocalDateTime;

public record QueryHotelsResponse(
        String otaHotelId,
        String name,
        String iataCode,
        Double longitude,
        Double latitude,
        String chainCode,
        String countryCode,
        Long dupeId,
        LocalDateTime otaLastUpdate

) {


}
