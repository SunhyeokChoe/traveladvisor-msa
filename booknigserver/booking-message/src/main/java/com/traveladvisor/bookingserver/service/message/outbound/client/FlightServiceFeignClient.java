package com.traveladvisor.bookingserver.service.message.outbound.client;

import com.traveladvisor.bookingserver.service.domain.dto.query.QueryFlightOffersResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.traveladvisor.common.domain.constant.common.TraceConstants.CORRELATION_ID;

@FeignClient(name="flight", url = "http://flight:9600"/*, fallback = FlightServiceFeignFallback.class*/)
public interface FlightServiceFeignClient {

    /**
     * 예약 가능 항공권을 조회합니다.
     *
     * @param correlationId 요청 추적을 위한 상관관계 ID
     * @param flightOfferId 예약 가능 항공권 ID
     * @return
     */
    @GetMapping(value = "/api/flight-offers/{id}",consumes = "application/json")
    ResponseEntity<QueryFlightOffersResponse> queryFlightOffer(@RequestHeader(CORRELATION_ID) String correlationId,
                                                               @PathVariable("id") Long flightOfferId);

}
