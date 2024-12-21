package com.traveladvisor.hotelserver.service.domain.mapper;

import com.traveladvisor.hotelserver.service.domain.dto.query.QueryHotelsResponse;
import com.traveladvisor.hotelserver.service.domain.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelMapper {

    /**
     * Hotel -> QueryHotelsResponse
     *
     * @param hotel
     * @return
     */
    public QueryHotelsResponse toQueryHotelsResponse(Hotel hotel) {
        return new QueryHotelsResponse(
            hotel.getOtaHotelId(),
            hotel.getName(),
            hotel.getIataCode(),
            hotel.getLongitude(),
            hotel.getLatitude(),
            hotel.getChainCode(),
            hotel.getCountryCode(),
            hotel.getDupeId(),
            hotel.getOtaLastUpdate());
    }

}
