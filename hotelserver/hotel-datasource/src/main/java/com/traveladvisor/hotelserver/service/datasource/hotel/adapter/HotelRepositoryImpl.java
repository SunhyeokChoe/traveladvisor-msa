package com.traveladvisor.hotelserver.service.datasource.hotel.adapter;

import com.traveladvisor.hotelserver.service.datasource.hotel.mapper.HotelDatasourceMapper;
import com.traveladvisor.hotelserver.service.datasource.hotel.repository.HotelJpaRepository;
import com.traveladvisor.hotelserver.service.domain.entity.Hotel;
import com.traveladvisor.hotelserver.service.domain.port.output.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HotelRepositoryImpl implements HotelRepository {

    private final HotelJpaRepository hotelJpaRepository;

    private final HotelDatasourceMapper hotelDatasourceMapper;

    @Override
    public Page<Hotel> findAll(Pageable pageable) {
        return hotelJpaRepository.findAll(pageable).map(hotelDatasourceMapper::toDomain);
    }

}
