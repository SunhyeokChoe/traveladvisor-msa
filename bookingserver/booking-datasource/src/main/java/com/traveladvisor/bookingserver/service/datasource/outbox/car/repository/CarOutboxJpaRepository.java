package com.traveladvisor.bookingserver.service.datasource.outbox.car.repository;

import com.traveladvisor.bookingserver.service.datasource.outbox.car.entity.CarOutboxEntity;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CarOutboxJpaRepository extends JpaRepository<CarOutboxEntity, UUID> {

    Optional<CarOutboxEntity> findByEventTypeAndSagaActionIdAndSagaActionStatusIn(
            String eventType, UUID sagaActionId, List<SagaActionStatus> sagaActionStatus);

}
