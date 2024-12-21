package com.traveladvisor.flightserver.service.datasource.flight.mapper;

import com.traveladvisor.common.datasource.flight.entity.FlightOfferEntity;
import com.traveladvisor.common.domain.vo.FlightOfferId;
import com.traveladvisor.flightserver.service.domain.entity.FlightOffer;
import org.springframework.stereotype.Component;

@Component
public class FlightOfferDatasourceMapper {

    /**
     * FlightOfferEntity -> FlightOffer
     *
     * @param flightOfferEntity
     * @return FlightOffer
     */
    public FlightOffer toDomain(FlightOfferEntity flightOfferEntity) {
        return FlightOffer.builder()
                .id(new FlightOfferId(flightOfferEntity.getId()))
                .carrierCode(flightOfferEntity.getCarrierCode())
                .operatingCarrierCode(flightOfferEntity.getOperatingCarrierCode())
                .flightNumber(flightOfferEntity.getFlightNumber())
                .aircraftCode(flightOfferEntity.getAircraftCode())
                .departureIata(flightOfferEntity.getDepartureIata())
                .departureAt(flightOfferEntity.getDepartureAt())
                .arrivalIata(flightOfferEntity.getArrivalIata())
                .arrivalAt(flightOfferEntity.getArrivalAt())
                .duration(flightOfferEntity.getDuration())
                .blacklistedInEu(flightOfferEntity.getBlacklistedInEu())
                .numberOfStops(flightOfferEntity.getNumberOfStops())
                .price(flightOfferEntity.getPrice())
                .build();
    }

    /**
     * FlightOffer -> FlightOfferEntity
     *
     * @param flightOffer
     * @return FlightOfferEntity
     */
    public FlightOfferEntity toEntity(FlightOffer flightOffer) {
        return FlightOfferEntity.builder()
                .carrierCode(flightOffer.getCarrierCode())
                .operatingCarrierCode(flightOffer.getOperatingCarrierCode())
                .flightNumber(flightOffer.getFlightNumber())
                .aircraftCode(flightOffer.getAircraftCode())
                .departureIata(flightOffer.getDepartureIata())
                .departureAt(flightOffer.getDepartureAt())
                .arrivalIata(flightOffer.getArrivalIata())
                .arrivalAt(flightOffer.getArrivalAt())
                .duration(flightOffer.getDuration())
                .blacklistedInEu(flightOffer.getBlacklistedInEu())
                .numberOfStops(flightOffer.getNumberOfStops())
                .price(flightOffer.getPrice())
                .build();
    }
}
