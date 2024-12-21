package com.traveladvisor.carserver.service.domain.usecase.query;

import com.traveladvisor.car.service.domain.dto.query.QueryCarOffersResponse;
import com.traveladvisor.car.service.domain.mapper.CarOfferMapper;
import com.traveladvisor.car.service.domain.port.output.repository.CarOfferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class CarQueryHandler {

    private final CarOfferRepository carOfferRepository;
    private final CarOfferMapper carOfferMapper;

    public Optional<QueryCarOffersResponse> queryCarOffer(Long carOfferId) {
        return carOfferRepository.findById(carOfferId).map(carOfferMapper::toQueryCarOffersResponse);
    }

    public Page<QueryCarOffersResponse> queryCarOffers(Pageable pageable) {
        return carOfferRepository.findAll(pageable).map(carOfferMapper::toQueryCarOffersResponse);
    }

}
