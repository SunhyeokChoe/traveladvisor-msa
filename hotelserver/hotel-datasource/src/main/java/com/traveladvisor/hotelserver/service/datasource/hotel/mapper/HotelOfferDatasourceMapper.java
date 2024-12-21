package com.traveladvisor.hotelserver.service.datasource.hotel.mapper;

import com.traveladvisor.common.datasource.hotel.entity.HotelOfferEntity;
import com.traveladvisor.common.domain.vo.HotelId;
import com.traveladvisor.common.domain.vo.HotelOfferId;
import com.traveladvisor.hotelserver.service.domain.entity.HotelOffer;
import org.springframework.stereotype.Component;

@Component
public class HotelOfferDatasourceMapper {

    /**
     * HotelOfferEntity -> HotelOffer
     *
     * @param hotelOfferEntity
     * @return HotelOffer
     */
    public HotelOffer toDomain(HotelOfferEntity hotelOfferEntity) {
        return HotelOffer.builder()
                .id(new HotelOfferId(hotelOfferEntity.getId()))
                .hotelId(new HotelId(hotelOfferEntity.getHotelId()))
                .checkInDate(hotelOfferEntity.getCheckInDate())
                .checkOutDate(hotelOfferEntity.getCheckOutDate())
                .rateCode(hotelOfferEntity.getRateCode())
                .rateFamilyCode(hotelOfferEntity.getRateFamilyCode())
                .rateFamilyType(hotelOfferEntity.getRateFamilyType())
                .roomType(hotelOfferEntity.getRoomType())
                .roomCategory(hotelOfferEntity.getRoomCategory())
                .roomBeds(hotelOfferEntity.getRoomBeds())
                .roomBedType(hotelOfferEntity.getRoomBedType())
                .roomDescriptionText(hotelOfferEntity.getRoomDescriptionText())
                .roomDescriptionLang(hotelOfferEntity.getRoomDescriptionLang())
                .guestsAdults(hotelOfferEntity.getGuestsAdults())
                .priceCurrency(hotelOfferEntity.getPriceCurrency())
                .priceBase(hotelOfferEntity.getPriceBase())
                .priceTotal(hotelOfferEntity.getPriceTotal())
                .priceVariations(hotelOfferEntity.getPriceVariations())
                .cancellationPolicies(hotelOfferEntity.getCancellationPolicies())
                .paymentType(hotelOfferEntity.getPaymentType())
                .build();
    }

    /**
     * HotelOffer -> HotelOfferEntity
     *
     * @param hotelOffer
     * @return HotelOfferEntity
     */
    public HotelOfferEntity toEntity(HotelOffer hotelOffer) {
        return HotelOfferEntity.builder()
                .hotelId(hotelOffer.getHotelId().getValue())
                .checkInDate(hotelOffer.getCheckInDate())
                .checkOutDate(hotelOffer.getCheckOutDate())
                .rateCode(hotelOffer.getRateCode())
                .rateFamilyCode(hotelOffer.getRateFamilyCode())
                .rateFamilyType(hotelOffer.getRateFamilyType())
                .roomType(hotelOffer.getRoomType())
                .roomCategory(hotelOffer.getRoomCategory())
                .roomBeds(hotelOffer.getRoomBeds())
                .roomBedType(hotelOffer.getRoomBedType())
                .roomDescriptionText(hotelOffer.getRoomDescriptionText())
                .roomDescriptionLang(hotelOffer.getRoomDescriptionLang())
                .guestsAdults(hotelOffer.getGuestsAdults())
                .priceCurrency(hotelOffer.getPriceCurrency())
                .priceBase(hotelOffer.getPriceBase())
                .priceTotal(hotelOffer.getPriceTotal())
                .priceVariations(hotelOffer.getPriceVariations())
                .cancellationPolicies(hotelOffer.getCancellationPolicies())
                .paymentType(hotelOffer.getPaymentType())
                .build();
    }

}
