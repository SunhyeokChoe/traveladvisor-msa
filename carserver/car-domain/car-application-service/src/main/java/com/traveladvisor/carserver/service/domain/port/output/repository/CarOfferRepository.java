package com.traveladvisor.carserver.service.domain.port.output.repository;

import com.traveladvisor.car.service.domain.entity.CarOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CarOfferRepository {

    Optional<CarOffer> findById(Long carOfferId);

    Page<CarOffer> findAll(Pageable pageable);

}
