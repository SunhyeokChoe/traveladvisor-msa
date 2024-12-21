package com.traveladvisor.carserver.service.domain.mapper;

import com.traveladvisor.carserver.service.domain.dto.query.QueryCarOffersResponse;
import com.traveladvisor.carserver.service.domain.entity.CarOffer;
import org.springframework.stereotype.Component;

@Component
public class CarOfferMapper {

    /**
     * CarOffer -> QueryCarOffersResponse
     *
     * @param carOffer CarOffer
     * @return QueryCarOffersResponse
     */
    public QueryCarOffersResponse toQueryCarOffersResponse(CarOffer carOffer) {
        return new QueryCarOffersResponse(
                carOffer.getId().getValue(),
                carOffer.getVehicleCode(),
                carOffer.getCategory(),
                carOffer.getDescription(),
                carOffer.getImageUrl(),
                carOffer.getBaggages(),
                carOffer.getSeats(),
                carOffer.getPrice()
        );
    }

}
