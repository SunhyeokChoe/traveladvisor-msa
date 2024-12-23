package com.traveladvisor.flightserver.service.datasource.flight.repository;

import com.traveladvisor.flightserver.service.datasource.entity.BookingApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingApprovalJpaRepository extends JpaRepository<BookingApprovalEntity, UUID> {

}
