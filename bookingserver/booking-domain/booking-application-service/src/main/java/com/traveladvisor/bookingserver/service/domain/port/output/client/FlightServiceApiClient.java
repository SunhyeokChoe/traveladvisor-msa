package com.traveladvisor.bookingserver.service.domain.port.output.client;

import com.traveladvisor.bookingserver.service.domain.dto.query.QueryFlightOffersResponse;
import org.springframework.http.ResponseEntity;

public interface FlightServiceApiClient {

    /**
     * 예약 가능 항공권 정보를 조회합니다.
     *
     * @param flightOfferId 예약 가능 항공권 ID
     * @return
     */
    ResponseEntity<QueryFlightOffersResponse> queryFlightOffer(String correlationId, Long flightOfferId);

}
