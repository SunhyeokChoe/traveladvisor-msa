package com.traveladvisor.bookingserver.service.datasource.outbox.hotel.adapter;

import com.traveladvisor.bookingserver.service.datasource.outbox.hotel.mapper.HotelOutboxDatasourceMapper;
import com.traveladvisor.bookingserver.service.datasource.outbox.hotel.repository.HotelOutboxJpaRepository;
import com.traveladvisor.bookingserver.service.domain.outbox.model.hotel.HotelOutbox;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.HotelOutboxRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HotelOutboxRepositoryImpl implements HotelOutboxRepository {

    private final HotelOutboxJpaRepository HotelOutboxJpaRepository;
    private final HotelOutboxDatasourceMapper hotelOutboxDatasourceMapper;

    @Override
    public HotelOutbox save(HotelOutbox HotelOutbox) {
        return hotelOutboxDatasourceMapper.toDomain(
            HotelOutboxJpaRepository.save(hotelOutboxDatasourceMapper.toEntity(HotelOutbox)));
    }

}
