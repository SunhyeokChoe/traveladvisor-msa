package com.traveladvisor.bookingserver.service.message.outbound.adapter;

import com.traveladvisor.bookingserver.service.domain.dto.query.QueryFlightOffersResponse;
import com.traveladvisor.bookingserver.service.domain.port.output.client.FlightServiceApiClient;
import com.traveladvisor.bookingserver.service.message.outbound.client.FlightServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FlightServiceApiClientImpl implements FlightServiceApiClient {

    private final FlightServiceFeignClient flightServiceFeignClient;

    @Override
    public ResponseEntity<QueryFlightOffersResponse> queryFlightOffer(String correlationId, Long flightOfferId) {
        return flightServiceFeignClient.queryFlightOffer(correlationId, flightOfferId);
    }

}
