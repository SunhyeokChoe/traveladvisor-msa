package com.traveladvisor.flightserver.service.datasource.flight.adapter;

import com.traveladvisor.flightserver.service.datasource.flight.mapper.FlightOfferDatasourceMapper;
import com.traveladvisor.flightserver.service.datasource.flight.repository.FlightOfferJpaRepository;
import com.traveladvisor.flightserver.service.domain.entity.FlightOffer;
import com.traveladvisor.flightserver.service.domain.port.output.repository.FlightOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class FlightOfferRepositoryImpl implements FlightOfferRepository {

    private final FlightOfferJpaRepository flightOfferJpaRepository;
    private final FlightOfferDatasourceMapper flightOfferDatasourceMapper;

    @Override
    public Optional<FlightOffer> findById(Long flightOfferId) {
        return flightOfferJpaRepository.findById(flightOfferId).map(flightOfferDatasourceMapper::toDomain);
    }

    @Override
    public Page<FlightOffer> findAll(Pageable pageable) {
        return flightOfferJpaRepository.findAll(pageable).map(flightOfferDatasourceMapper::toDomain);
    }

}
