package com.traveladvisor.batchserver.service.message.sender.client;

import com.traveladvisor.common.message.config.FeignFormEncoderConfig;
import com.traveladvisor.common.message.dto.query.amadeus.QueryAccessToken;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(
        name = "amadeus-client",
        url = "${batch.amadeus.base-url}",
        configuration = FeignFormEncoderConfig.class
)
public interface AmadeusFeignClient {

    @PostMapping(
            value = "${batch.amadeus.endpoints.security.token}",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<Map<String, Object>> getAccessToken(@RequestBody QueryAccessToken queryAccessToken);

    /**
     * 파라미터 cityCode 는 Amadeus API 스펙상 국가 ISO Code 인 것 처럼 보이나, 실제로는 IATA Code 를 전달해야 합니다.
     *
     * @param authorization 인증 헤더
     * @param cityCode      IATA Code (주의! 국가 ISO Code 아님)
     * @return
     */
    @GetMapping("${batch.amadeus.endpoints.reference-data.location.hotels.by-city}")
    ResponseEntity<String> getHotelsByCityCode(@RequestHeader("Authorization") String authorization,
                                               @RequestParam("cityCode")       String cityCode);

}
