package com.traveladvisor.batchserver.service.domain.port.output.client;

import com.traveladvisor.common.message.dto.query.amadeus.QueryAccessToken;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface OtaApiClient {

    ResponseEntity<Map<String, Object>> getAccessToken(QueryAccessToken queryAccessToken);

    ResponseEntity<String> getHotelsByCityCode(String authorization, String iataCode);

}
