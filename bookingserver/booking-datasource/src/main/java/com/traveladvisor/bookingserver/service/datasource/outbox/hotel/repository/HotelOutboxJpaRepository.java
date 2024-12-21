package com.traveladvisor.bookingserver.service.datasource.outbox.hotel.repository;

import com.traveladvisor.bookingserver.service.datasource.outbox.hotel.entity.HotelOutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HotelOutboxJpaRepository extends JpaRepository<HotelOutboxEntity, UUID> {

}
