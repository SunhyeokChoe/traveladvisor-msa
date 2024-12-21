package com.traveladvisor.flightserver.service.domain.port.output.repository;

import com.traveladvisor.flightserver.service.domain.entity.FlightOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FlightOfferRepository {

    Optional<FlightOffer> findById(Long flightOfferId);

    Page<FlightOffer> findAll(Pageable pageable);

}
