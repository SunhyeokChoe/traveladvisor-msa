package com.traveladvisor.bookingserver.service.datasource.outbox.flight.adapter;

import com.traveladvisor.bookingserver.service.datasource.outbox.flight.mapper.FlightOutboxDatasourceMapper;
import com.traveladvisor.bookingserver.service.datasource.outbox.flight.repository.FlightOutboxJpaRepository;
import com.traveladvisor.bookingserver.service.domain.outbox.model.flight.FlightOutbox;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.FlightOutboxRepository;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class FlightOutboxRepositoryImpl implements FlightOutboxRepository {

    private FlightOutboxJpaRepository flightOutboxJpaRepository;
    private final FlightOutboxDatasourceMapper flightOutboxDatasourceMapper;

    @Override
    public FlightOutbox save(FlightOutbox flightOutbox) {

        return flightOutboxDatasourceMapper.toDomain(
                flightOutboxJpaRepository.save(flightOutboxDatasourceMapper.toEntity(flightOutbox)));
    }

    @Override
    public Optional<FlightOutbox> findByEventTypeAndSagaActionIdAndSagaActionStatusIn(
            String eventType, UUID sagaActionId, SagaActionStatus... sagaActionStatuses) {

        return flightOutboxJpaRepository
                .findByEventTypeAndSagaActionIdAndSagaActionStatusIn(
                        eventType, sagaActionId, Arrays.asList(sagaActionStatuses))
                .map(flightOutboxDatasourceMapper::toDomain);
    }

}
