package com.traveladvisor.hotelserver.service.datasource.hotel.repository;

import com.traveladvisor.common.datasource.hotel.entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelJpaRepository extends JpaRepository<HotelEntity, Long> {

}
