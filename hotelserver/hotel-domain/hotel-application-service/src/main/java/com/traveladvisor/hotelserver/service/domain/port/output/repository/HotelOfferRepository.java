package com.traveladvisor.hotelserver.service.domain.port.output.repository;

import com.traveladvisor.hotelserver.service.domain.entity.HotelOffer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface HotelOfferRepository {

    /**
     * 예약 가능 호텔 객실을 조회합니다.
     *
     * @param hotelOfferId
     * @return Optional<HotelOffer>
     */
    Optional<HotelOffer> findById(Long hotelOfferId);

    /**
     * 예약 가능 호텔 객실 목록을 조회합니다.
     *
     * @param pageable
     * @return Page<HotelOffer>
     */
    Page<HotelOffer> findAll(Pageable pageable);

}
