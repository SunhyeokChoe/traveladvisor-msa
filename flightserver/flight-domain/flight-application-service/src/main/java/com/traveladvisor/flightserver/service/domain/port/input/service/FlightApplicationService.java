package com.traveladvisor.flightserver.service.domain.port.input.service;

import com.traveladvisor.flightserver.service.domain.dto.query.QueryFlightOffersResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface FlightApplicationService {

    /**
     * 예약 가능 항공권을 조회합니다.
     *
     * @param flightOfferId
     * @return FlightOffersQueryResponse
     */
    Optional<QueryFlightOffersResponse> queryFlightOffer(Long flightOfferId);

    /**
     * 예약 가능 항공권 목록을 조회합니다.
     *
     * @param pageable
     * @return FlightOffersQueryResponse
     */
    Page<QueryFlightOffersResponse> queryFlightOffers(Pageable pageable);

}
