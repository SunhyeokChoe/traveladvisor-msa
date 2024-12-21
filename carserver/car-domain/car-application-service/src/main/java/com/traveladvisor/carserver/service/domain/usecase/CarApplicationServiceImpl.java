package com.traveladvisor.carserver.service.domain.usecase;

import com.traveladvisor.car.service.domain.dto.query.QueryCarOffersResponse;
import com.traveladvisor.car.service.domain.port.input.service.CarApplicationService;
import com.traveladvisor.car.service.domain.usecase.query.CarQueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Validated
@Service
public class CarApplicationServiceImpl implements CarApplicationService {

    private final CarQueryHandler carQueryHandler;

    @Override
    public Optional<QueryCarOffersResponse> queryCarOffer(Long carOfferId) {
        return carQueryHandler.queryCarOffer(carOfferId);
    }


    @Override
    public Page<QueryCarOffersResponse> queryCarOffers(Pageable pageable) {
        return carQueryHandler.queryCarOffers(pageable);
    }

}
