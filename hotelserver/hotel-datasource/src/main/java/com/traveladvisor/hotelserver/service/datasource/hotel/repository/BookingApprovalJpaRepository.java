package com.traveladvisor.hotelserver.service.datasource.hotel.repository;

import com.traveladvisor.hotelserver.service.datasource.hotel.entity.BookingApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingApprovalJpaRepository extends JpaRepository<BookingApprovalEntity, UUID> {

}
