package com.traveladvisor.flightserver.service.domain.usecase;

import com.traveladvisor.flightserver.service.domain.dto.query.QueryFlightOffersResponse;
import com.traveladvisor.flightserver.service.domain.port.input.service.FlightApplicationService;
import com.traveladvisor.flightserver.service.domain.usecase.query.FlightQueryHandler;
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
public class FlightApplicationServiceImpl implements FlightApplicationService {

    private final FlightQueryHandler flightQueryHandler;

    @Override
    public Optional<QueryFlightOffersResponse> queryFlightOffer(Long flightOfferId) {
        return flightQueryHandler.queryFlightOffer(flightOfferId);
    }

    @Override
    public Page<QueryFlightOffersResponse> queryFlightOffers(Pageable pageable) {
        return flightQueryHandler.queryFlightOffers(pageable);
    }

}
