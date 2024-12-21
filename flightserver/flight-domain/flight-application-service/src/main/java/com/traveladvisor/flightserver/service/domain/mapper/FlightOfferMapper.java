package com.traveladvisor.flightserver.service.domain.mapper;

import com.traveladvisor.flightserver.service.domain.dto.query.QueryFlightOffersResponse;
import com.traveladvisor.flightserver.service.domain.entity.FlightOffer;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
public class FlightOfferMapper {

    /**
     * FlightOffer -> QueryFlightOffersResponse
     *
     * @param flightOffer
     * @return QueryFlightOffersResponse
     */
    public QueryFlightOffersResponse toQueryFlightOffersResponse(FlightOffer flightOffer) {
        return new QueryFlightOffersResponse(
                flightOffer.getId().getValue(),
                flightOffer.getCarrierCode(),
                flightOffer.getOperatingCarrierCode(),
                flightOffer.getFlightNumber(),
                flightOffer.getAircraftCode(),
                flightOffer.getDepartureIata(),
                ZonedDateTime.of(flightOffer.getDepartureAt(), ZonedDateTime.now().getZone()),
                flightOffer.getArrivalIata(),
                ZonedDateTime.of(flightOffer.getArrivalAt(), ZonedDateTime.now().getZone()),
                flightOffer.getDuration().toString(),
                flightOffer.getBlacklistedInEu(),
                flightOffer.getNumberOfStops(),
                flightOffer.getPrice()
        );
    }

}
