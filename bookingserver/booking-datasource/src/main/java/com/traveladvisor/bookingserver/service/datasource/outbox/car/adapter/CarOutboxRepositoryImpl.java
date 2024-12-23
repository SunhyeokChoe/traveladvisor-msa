package com.traveladvisor.bookingserver.service.datasource.outbox.car.adapter;

import com.traveladvisor.bookingserver.service.datasource.outbox.car.mapper.CarOutboxDatasourceMapper;
import com.traveladvisor.bookingserver.service.datasource.outbox.car.repository.CarOutboxJpaRepository;
import com.traveladvisor.bookingserver.service.domain.outbox.model.car.CarOutbox;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.CarOutboxRepository;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class CarOutboxRepositoryImpl implements CarOutboxRepository {

    private CarOutboxJpaRepository carOutboxJpaRepository;
    private final CarOutboxDatasourceMapper carOutboxDatasourceMapper;

    @Override
    public CarOutbox save(CarOutbox carOutbox) {

        return carOutboxDatasourceMapper.toDomain(
                carOutboxJpaRepository.save(carOutboxDatasourceMapper.toEntity(carOutbox)));
    }

    @Override
    public Optional<CarOutbox> findByEventTypeAndSagaActionIdAndSagaActionStatusIn(
            String eventType, UUID sagaActionId, SagaActionStatus... sagaActionStatuses) {

        return carOutboxJpaRepository
                .findByEventTypeAndSagaActionIdAndSagaActionStatusIn(
                        eventType, sagaActionId, Arrays.asList(sagaActionStatuses))
                .map(carOutboxDatasourceMapper::toDomain);
    }

}
