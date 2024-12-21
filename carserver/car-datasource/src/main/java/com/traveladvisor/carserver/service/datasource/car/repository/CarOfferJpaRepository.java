package com.traveladvisor.carserver.service.datasource.car.repository;

import com.traveladvisor.common.datasource.car.entity.CarOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarOfferJpaRepository extends JpaRepository<CarOfferEntity, Long> {

}
