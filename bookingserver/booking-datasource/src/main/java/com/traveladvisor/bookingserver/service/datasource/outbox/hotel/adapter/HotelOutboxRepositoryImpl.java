package com.traveladvisor.bookingserver.service.datasource.outbox.hotel.adapter;

import com.traveladvisor.bookingserver.service.datasource.outbox.hotel.mapper.HotelOutboxDatasourceMapper;
import com.traveladvisor.bookingserver.service.datasource.outbox.hotel.repository.HotelOutboxJpaRepository;
import com.traveladvisor.bookingserver.service.domain.outbox.model.hotel.HotelOutbox;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.HotelOutboxRepository;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HotelOutboxRepositoryImpl implements HotelOutboxRepository {

    private final HotelOutboxJpaRepository hotelOutboxJpaRepository;
    private final HotelOutboxDatasourceMapper hotelOutboxDatasourceMapper;

    @Override
    public HotelOutbox save(HotelOutbox HotelOutbox) {

        return hotelOutboxDatasourceMapper.toDomain(
                hotelOutboxJpaRepository.save(hotelOutboxDatasourceMapper.toEntity(HotelOutbox)));
    }

    @Override
    public Optional<HotelOutbox> findByEventTypeAndSagaActionIdAndSagaActionStatusIn(
            String eventType, UUID sagaActionId, SagaActionStatus... sagaActionStatuses) {

        return hotelOutboxJpaRepository
                .findByEventTypeAndSagaActionIdAndSagaActionStatusIn(
                        eventType, sagaActionId, Arrays.asList(sagaActionStatuses))
                .map(hotelOutboxDatasourceMapper::toDomain);
    }

}
