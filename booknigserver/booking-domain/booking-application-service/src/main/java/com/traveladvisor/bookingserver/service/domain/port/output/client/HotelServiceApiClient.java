package com.traveladvisor.bookingserver.service.domain.port.output.client;

import com.traveladvisor.bookingserver.service.domain.dto.query.QueryHotelOffersResponse;
import org.springframework.http.ResponseEntity;

public interface HotelServiceApiClient {

    /**
     * 예약 가능 호텔 객실 정보를 조회합니다.
     *
     * @param hotelOfferId 예약 가능 호텔 객실 ID
     * @return
     */
    ResponseEntity<QueryHotelOffersResponse> queryHotelOffer(String correlationId, Long hotelOfferId);

}
