package com.traveladvisor.hotelserver.service.domain.mapper;

import com.traveladvisor.hotelserver.service.domain.dto.query.QueryHotelOffersResponse;
import com.traveladvisor.hotelserver.service.domain.entity.HotelOffer;
import org.springframework.stereotype.Component;

@Component
public class HotelOfferMapper {

    /**
     * HotelOffer -> QueryHotelOffersResponse
     *
     * @param hotelOffer
     * @return QueryHotelOffersResponse
     */
    public QueryHotelOffersResponse toQueryHotelOffersResponse(HotelOffer hotelOffer) {
        return new QueryHotelOffersResponse(
                hotelOffer.getId().getValue(),
                hotelOffer.getHotelId().getValue(),
                hotelOffer.getCheckInDate(),
                hotelOffer.getCheckOutDate(),
                hotelOffer.getRateCode(),
                hotelOffer.getRateFamilyCode(),
                hotelOffer.getRateFamilyType(),
                hotelOffer.getRoomType(),
                hotelOffer.getRoomCategory(),
                hotelOffer.getRoomBeds(),
                hotelOffer.getRoomBedType(),
                hotelOffer.getRoomDescriptionText(),
                hotelOffer.getRoomDescriptionLang(),
                hotelOffer.getGuestsAdults(),
                hotelOffer.getPriceCurrency(),
                hotelOffer.getPriceBase(),
                hotelOffer.getPriceTotal(),
                hotelOffer.getPriceVariations(),
                hotelOffer.getCancellationPolicies(),
                hotelOffer.getPaymentType()

        );
    }

}
