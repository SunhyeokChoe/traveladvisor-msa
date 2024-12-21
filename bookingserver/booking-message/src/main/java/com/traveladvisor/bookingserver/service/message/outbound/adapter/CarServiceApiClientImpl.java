package com.traveladvisor.bookingserver.service.message.outbound.adapter;

import com.traveladvisor.bookingserver.service.domain.dto.query.QueryCarOffersResponse;
import com.traveladvisor.bookingserver.service.domain.port.output.client.CarServiceApiClient;
import com.traveladvisor.bookingserver.service.message.outbound.client.CarServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CarServiceApiClientImpl implements CarServiceApiClient {

    private final CarServiceFeignClient carServiceFeignClient;

    @Override
    public ResponseEntity<QueryCarOffersResponse> queryCarOffer(String correlationId, Long carOfferId) {
        return carServiceFeignClient.queryCarOffer(correlationId, carOfferId);
    }

}
