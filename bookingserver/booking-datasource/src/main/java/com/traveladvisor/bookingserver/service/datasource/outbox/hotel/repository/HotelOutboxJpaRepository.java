package com.traveladvisor.bookingserver.service.datasource.outbox.hotel.repository;

import com.traveladvisor.bookingserver.service.datasource.outbox.hotel.entity.HotelOutboxEntity;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HotelOutboxJpaRepository extends JpaRepository<HotelOutboxEntity, UUID> {

    Optional<HotelOutboxEntity> findByEventTypeAndSagaActionIdAndSagaActionStatusIn(
            String eventType, UUID sagaActionId, List<SagaActionStatus> sagaActionStatus);

}
