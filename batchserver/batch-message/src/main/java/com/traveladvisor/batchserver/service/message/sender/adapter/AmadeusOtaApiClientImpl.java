package com.traveladvisor.batchserver.service.message.sender.adapter;

import com.traveladvisor.batchserver.service.domain.port.output.client.OtaApiClient;
import com.traveladvisor.batchserver.service.message.sender.client.AmadeusFeignClient;
import com.traveladvisor.common.message.dto.query.amadeus.QueryAccessToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@RequiredArgsConstructor
@Component
public class AmadeusOtaApiClientImpl implements OtaApiClient {

    private final AmadeusFeignClient amadeusFeignClient;

    @Override
    public ResponseEntity<Map<String, Object>> getAccessToken(QueryAccessToken queryAccessToken) {
        return amadeusFeignClient.getAccessToken(queryAccessToken);
    }

    @Override
    public ResponseEntity<String> getHotelsByCityCode(String authorization, String iataCode) {
        return amadeusFeignClient.getHotelsByCityCode(authorization, iataCode);
    }

}
