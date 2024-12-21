package com.traveladvisor.bookingserver.service.message.outbound.client;

import com.traveladvisor.bookingserver.service.domain.dto.query.QueryCarOffersResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.traveladvisor.common.domain.constant.common.TraceConstants.CORRELATION_ID;

@FeignClient(name="car", url = "http://car:9700"/*, fallback = CarServiceFeignFallback.class*/)
public interface CarServiceFeignClient {

    /**
     * 예약 가능 차량을 조회합니다.
     *
     * @param correlationId 요청 추적을 위한 상관관계 ID
     * @param carOfferId    예약 가능 차량 ID
     * @return
     */
    @GetMapping(value = "/api/car-offers/{id}",consumes = "application/json")
    ResponseEntity<QueryCarOffersResponse> queryCarOffer(@RequestHeader(CORRELATION_ID) String correlationId,
                                                         @PathVariable("id") Long carOfferId);

}
