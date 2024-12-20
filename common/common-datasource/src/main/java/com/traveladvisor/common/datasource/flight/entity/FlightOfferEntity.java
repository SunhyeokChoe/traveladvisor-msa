package com.traveladvisor.common.datasource.flight.entity;

import com.traveladvisor.common.datasource.common.entity.BaseZonedDateTimeEntity;
import com.traveladvisor.common.datasource.converter.DurationToIntervalConverter;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "flight_offers", schema = "flight")
@Entity
public class FlightOfferEntity extends BaseZonedDateTimeEntity {

    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "carrier_code")
    private String carrierCode;

    @Column(name = "operating_carrier_code")
    private String operatingCarrierCode;

    @Column(name = "flight_number")
    private String flightNumber;

    @Column(name = "aircraft_code")
    private String aircraftCode;

    @Column(name = "departure_iata")
    private String departureIata;

    @Column(name = "departure_at")
    private LocalDateTime departureAt;

    @Column(name = "arrival_iata")
    private String arrivalIata;

    @Column(name = "arrival_at")
    private LocalDateTime arrivalAt;

    @Convert(converter = DurationToIntervalConverter.class)
    @Column(name = "duration")
    private Duration duration;

    @Column(name = "blacklisted_in_eu")
    private Boolean blacklistedInEu;

    @Column(name = "number_of_stops")
    private Integer numberOfStops;

    @Column(name = "price")
    private BigDecimal price;

}
