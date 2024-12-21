package com.traveladvisor.bookingserver.service.message.outbound.client;

import com.traveladvisor.bookingserver.service.domain.dto.query.QueryHotelOffersResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.traveladvisor.common.domain.constant.common.TraceConstants.CORRELATION_ID;

@FeignClient(name="hotel", url = "http://hotel:9500"/*, fallback = HotelServiceFeignFallback.class*/)
public interface HotelServiceFeignClient {

    /**
     * 예약 가능 호텔 객실을 조회합니다.
     *
     * @param correlationId 요청 추적을 위한 상관관계 ID
     * @param hotelOfferId  예약 가능 호텔 객실 ID
     * @return
     */
    @GetMapping(value = "/api/hotel-offers/{id}",consumes = "application/json")
    ResponseEntity<QueryHotelOffersResponse> queryHotelOffer(@RequestHeader(CORRELATION_ID) String correlationId,
                                                             @PathVariable("id") Long hotelOfferId);

}
