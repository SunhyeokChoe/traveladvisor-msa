package com.traveladvisor.carserver.service.datasource.car.adapter;

import com.traveladvisor.carserver.service.datasource.car.mapper.CarOfferDatasourceMapper;
import com.traveladvisor.carserver.service.datasource.car.repository.CarOfferJpaRepository;
import com.traveladvisor.carserver.service.domain.entity.CarOffer;
import com.traveladvisor.carserver.service.domain.port.output.repository.CarOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CarOfferRepositoryImpl implements CarOfferRepository {

    private final CarOfferJpaRepository carOfferJpaRepository;
    private final CarOfferDatasourceMapper carOfferDatasourceMapper;

    @Override
    public Optional<CarOffer> findById(Long carOfferId) {
        return carOfferJpaRepository.findById(carOfferId).map(carOfferDatasourceMapper::toDomain);
    }

    @Override
    public Page<CarOffer> findAll(Pageable pageable) {
        return carOfferJpaRepository.findAll(pageable).map(carOfferDatasourceMapper::toDomain);
    }

}
