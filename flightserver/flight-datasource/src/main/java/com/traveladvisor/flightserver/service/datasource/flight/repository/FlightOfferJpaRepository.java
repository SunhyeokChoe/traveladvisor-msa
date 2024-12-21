package com.traveladvisor.flightserver.service.datasource.flight.repository;

import com.traveladvisor.common.datasource.flight.entity.FlightOfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlightOfferJpaRepository extends JpaRepository<FlightOfferEntity, Long> {

}
