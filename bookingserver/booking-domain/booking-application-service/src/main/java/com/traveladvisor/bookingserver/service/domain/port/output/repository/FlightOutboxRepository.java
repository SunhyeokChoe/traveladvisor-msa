package com.traveladvisor.bookingserver.service.domain.port.output.repository;

import com.traveladvisor.bookingserver.service.domain.outbox.model.flight.FlightOutbox;
import com.traveladvisor.common.domain.saga.SagaActionStatus;

import java.util.Optional;
import java.util.UUID;

public interface FlightOutboxRepository {

    FlightOutbox save(FlightOutbox flightOutbox);

    Optional<FlightOutbox> findByEventTypeAndSagaActionIdAndSagaActionStatusIn(String eventType,
                                                                               UUID sagaActionId,
                                                                               SagaActionStatus... sagaActionStatuses);

}
