package com.traveladvisor.bookingserver.service.datasource.booking.repository;

import com.traveladvisor.bookingserver.service.datasource.booking.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingJpaRepository extends JpaRepository<BookingEntity, UUID> {

    Optional<BookingEntity> findByTraceId(UUID traceId);

}
