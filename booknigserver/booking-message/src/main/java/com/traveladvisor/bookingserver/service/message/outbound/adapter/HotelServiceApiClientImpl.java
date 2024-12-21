package com.traveladvisor.bookingserver.service.message.outbound.adapter;

import com.traveladvisor.bookingserver.service.domain.dto.query.QueryHotelOffersResponse;
import com.traveladvisor.bookingserver.service.domain.port.output.client.HotelServiceApiClient;
import com.traveladvisor.bookingserver.service.message.outbound.client.HotelServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HotelServiceApiClientImpl implements HotelServiceApiClient {

    private final HotelServiceFeignClient hotelServiceFeignClient;

    @Override
    public ResponseEntity<QueryHotelOffersResponse> queryHotelOffer(String correlationId, Long hotelOfferId) {
        return hotelServiceFeignClient.queryHotelOffer(correlationId, hotelOfferId);
    }

}
