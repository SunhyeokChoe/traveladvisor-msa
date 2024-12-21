package com.traveladvisor.bookingserver.service.domain.dto.query;

import java.math.BigDecimal;
import java.time.LocalDate;

public record QueryHotelOffersResponse(
        Long id,
        Long hotelId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        String rateCode,
        String rateFamilyCode,
        String rateFamilyType,
        String roomType,
        String roomCategory,
        Integer roomBeds,
        String roomBedType,
        String roomDescriptionText,
        String roomDescriptionLang,
        Integer guestsAdults,
        String priceCurrency,
        BigDecimal priceBase,
        BigDecimal priceTotal,
        String priceVariations,
        String cancellationPolicies,
        String paymentType

) {
}
