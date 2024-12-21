package com.traveladvisor.bookingserver.service.domain.port.output.client;

import com.traveladvisor.bookingserver.service.domain.dto.query.QueryCarOffersResponse;
import org.springframework.http.ResponseEntity;

public interface CarServiceApiClient {

    /**
     * 예약 가능 차량 정보를 조회합니다.
     *
     * @param carOfferId 예약 가능 차량 ID
     * @return
     */
    ResponseEntity<QueryCarOffersResponse> queryCarOffer(String correlationId, Long carOfferId);

}
