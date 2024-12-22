package com.traveladvisor.batchserver.service.datasource.hotel.repository;

import com.traveladvisor.common.datasource.hotel.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryJpaRepository extends JpaRepository<CountryEntity, Long> {

}
