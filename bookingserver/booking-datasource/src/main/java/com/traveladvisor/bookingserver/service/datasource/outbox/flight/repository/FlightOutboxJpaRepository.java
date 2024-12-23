package com.traveladvisor.bookingserver.service.datasource.outbox.flight.repository;

import com.traveladvisor.bookingserver.service.datasource.outbox.flight.entity.FlightOutboxEntity;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FlightOutboxJpaRepository extends JpaRepository<FlightOutboxEntity, UUID> {

    Optional<FlightOutboxEntity> findByEventTypeAndSagaActionIdAndSagaActionStatusIn(
            String eventType, UUID sagaActionId, List<SagaActionStatus> sagaActionStatus);

}
