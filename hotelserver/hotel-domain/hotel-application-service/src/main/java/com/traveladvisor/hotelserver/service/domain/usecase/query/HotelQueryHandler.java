package com.traveladvisor.hotelserver.service.domain.usecase.query;

import com.traveladvisor.hotelserver.service.domain.dto.query.QueryHotelOffersResponse;
import com.traveladvisor.hotelserver.service.domain.dto.query.QueryHotelsResponse;
import com.traveladvisor.hotelserver.service.domain.mapper.HotelMapper;
import com.traveladvisor.hotelserver.service.domain.mapper.HotelOfferMapper;
import com.traveladvisor.hotelserver.service.domain.port.output.repository.HotelOfferRepository;
import com.traveladvisor.hotelserver.service.domain.port.output.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class HotelQueryHandler {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    private final HotelOfferRepository hotelOfferRepository;
    private final HotelOfferMapper hotelOfferMapper;

    public Page<QueryHotelsResponse> queryHotels(Pageable pageable) {
        return hotelRepository.findAll(pageable).map(hotelMapper::toQueryHotelsResponse);
    }

    public Optional<QueryHotelOffersResponse> queryHotelOffer(Long hotelOfferId) {
        return hotelOfferRepository.findById(hotelOfferId).map(hotelOfferMapper::toQueryHotelOffersResponse);
    }

    public Page<QueryHotelOffersResponse> queryHotelOffers(Pageable pageable) {
        return hotelOfferRepository.findAll(pageable).map(hotelOfferMapper::toQueryHotelOffersResponse);
    }

}
