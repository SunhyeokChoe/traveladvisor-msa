package com.traveladvisor.carserver.service.datasource.car.repository;

import com.traveladvisor.carserver.service.datasource.car.entity.BookingApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingApprovalJpaRepository extends JpaRepository<BookingApprovalEntity, UUID> {

}
