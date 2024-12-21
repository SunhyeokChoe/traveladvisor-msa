package com.traveladvisor.carserver.service.domain.port.input.service;

import com.traveladvisor.carserver.service.domain.dto.query.QueryCarOffersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CarApplicationService {

    /**
     * 예약 가능 차량을 조회합니다.
     *
     * @param carOfferId
     * @return CarOffersQueryResponse
     */
    Optional<QueryCarOffersResponse> queryCarOffer(Long carOfferId);

    /**
     * 예약 가능 차량 목록을 조회합니다.
     *
     * @param pageable
     * @return CarOffersQueryResponse
     */
    Page<QueryCarOffersResponse> queryCarOffers(Pageable pageable);

}
