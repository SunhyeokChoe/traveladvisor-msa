package com.traveladvisor.hotelserver.service.domain.usecase;

import com.traveladvisor.hotelserver.service.domain.dto.query.QueryHotelOffersResponse;
import com.traveladvisor.hotelserver.service.domain.dto.query.QueryHotelsResponse;
import com.traveladvisor.hotelserver.service.domain.port.input.service.HotelApplicationService;
import com.traveladvisor.hotelserver.service.domain.usecase.query.HotelQueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Validated
@Service
public class HotelApplicationServiceImpl implements HotelApplicationService {

    private final HotelQueryHandler hotelQueryHandler;

    @Override
    public Page<QueryHotelsResponse> queryHotels(Pageable pageable) {
        return hotelQueryHandler.queryHotels(pageable);
    }

    @Override
    public Optional<QueryHotelOffersResponse> queryHotelOffer(Long hotelOfferId) {
        return hotelQueryHandler.queryHotelOffer(hotelOfferId);
    }

    @Override
    public Page<QueryHotelOffersResponse> queryHotelOffers(Pageable pageable) {
        return hotelQueryHandler.queryHotelOffers(pageable);
    }

}
