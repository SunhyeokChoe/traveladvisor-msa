package com.traveladvisor.hotelserver.service.datasource.hotel.adapter;

import com.traveladvisor.hotelserver.service.datasource.hotel.mapper.HotelOfferDatasourceMapper;
import com.traveladvisor.hotelserver.service.datasource.hotel.repository.HotelOfferJpaRepository;
import com.traveladvisor.hotelserver.service.domain.entity.HotelOffer;
import com.traveladvisor.hotelserver.service.domain.port.output.repository.HotelOfferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class HotelOfferRepositoryImpl implements HotelOfferRepository {

    private final HotelOfferJpaRepository hotelOfferJpaRepository;
    private final HotelOfferDatasourceMapper hotelOfferDatasourceMapper;

    @Override
    public Optional<HotelOffer> findById(Long hotelOfferId) {
        return hotelOfferJpaRepository.findById(hotelOfferId).map(hotelOfferDatasourceMapper::toDomain);
    }

    @Override
    public Page<HotelOffer> findAll(Pageable pageable) {
        return hotelOfferJpaRepository.findAll(pageable).map(hotelOfferDatasourceMapper::toDomain);
    }

}
