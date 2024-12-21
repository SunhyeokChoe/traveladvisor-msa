package com.traveladvisor.hotelserver.service.domain.port.input.service;

import com.traveladvisor.hotelserver.service.domain.dto.query.QueryHotelOffersResponse;
import com.traveladvisor.hotelserver.service.domain.dto.query.QueryHotelsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface HotelApplicationService {

    /**
     * 호텔 메타데이터 목록을 조회합니다.
     *
     * @param pageable
     * @return HotelsQueryResponse
     */
    Page<QueryHotelsResponse> queryHotels(Pageable pageable);

    /**
     * 호텔 예약 가능 객실을 조회합니다.
     *
     * @param hotelOfferId
     * @return HotelOffersQueryResponse
     */
    Optional<QueryHotelOffersResponse> queryHotelOffer(Long hotelOfferId);

    /**
     * 호텔 예약 가능 객실 목록을 조회합니다.
     *
     * @param pageable
     * @return HotelOffersQueryResponse
     */
    Page<QueryHotelOffersResponse> queryHotelOffers(Pageable pageable);

}
