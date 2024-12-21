package com.traveladvisor.flightserver.service.domain.usecase.query;

import com.traveladvisor.flightserver.service.domain.dto.query.QueryFlightOffersResponse;
import com.traveladvisor.flightserver.service.domain.mapper.FlightOfferMapper;
import com.traveladvisor.flightserver.service.domain.port.output.repository.FlightOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class FlightQueryHandler {

    private final FlightOfferRepository flightOfferRepository;
    private final FlightOfferMapper flightOfferMapper;

    public Optional<QueryFlightOffersResponse> queryFlightOffer(Long flightOfferId) {
        return flightOfferRepository.findById(flightOfferId).map(flightOfferMapper::toQueryFlightOffersResponse);
    }

    public Page<QueryFlightOffersResponse> queryFlightOffers(Pageable pageable) {
        return flightOfferRepository.findAll(pageable).map(flightOfferMapper::toQueryFlightOffersResponse);
    }

}
