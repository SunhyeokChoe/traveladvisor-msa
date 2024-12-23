package com.traveladvisor.bookingserver.service.domain.port.output.repository;

import com.traveladvisor.bookingserver.service.domain.outbox.model.car.CarOutbox;
import com.traveladvisor.common.domain.saga.SagaActionStatus;

import java.util.Optional;
import java.util.UUID;

public interface CarOutboxRepository {

    CarOutbox save(CarOutbox flightOutbox);

    Optional<CarOutbox> findByEventTypeAndSagaActionIdAndSagaActionStatusIn(String eventType,
                                                                               UUID sagaActionId,
                                                                               SagaActionStatus... sagaActionStatuses);

}
