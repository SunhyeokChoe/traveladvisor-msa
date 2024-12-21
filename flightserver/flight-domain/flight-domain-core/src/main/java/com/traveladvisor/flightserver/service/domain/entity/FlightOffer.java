package com.traveladvisor.flightserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.AggregateRoot;
import com.traveladvisor.common.domain.vo.FlightOfferId;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * FlightOffer 도메인의 Aggregate Root입니다.
 * 항공편 예약 가능 오퍼 정보를 포함하는 핵심 엔터티입니다.
 */
public class FlightOffer extends AggregateRoot<FlightOfferId> {

    private final String carrierCode;
    private final String operatingCarrierCode;
    private final String flightNumber;
    private final String aircraftCode;
    private final String departureIata;
    private final LocalDateTime departureAt;
    private final String arrivalIata;
    private final LocalDateTime arrivalAt;
    private final Duration duration;
    private final Boolean blacklistedInEu;
    private final Integer numberOfStops;
    private final BigDecimal price;

    private FlightOffer(Builder builder) {
        super.setId(builder.flightOfferId);
        this.carrierCode = builder.carrierCode;
        this.operatingCarrierCode = builder.operatingCarrierCode;
        this.flightNumber = builder.flightNumber;
        this.aircraftCode = builder.aircraftCode;
        this.departureIata = builder.departureIata;
        this.departureAt = builder.departureAt;
        this.arrivalIata = builder.arrivalIata;
        this.arrivalAt = builder.arrivalAt;
        this.duration = builder.duration;
        this.blacklistedInEu = builder.blacklistedInEu;
        this.numberOfStops = builder.numberOfStops;
        this.price = builder.price;
    }

    // BEGIN: Getter
    public String getCarrierCode() {
        return carrierCode;
    }

    public String getOperatingCarrierCode() {
        return operatingCarrierCode;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAircraftCode() {
        return aircraftCode;
    }

    public String getDepartureIata() {
        return departureIata;
    }

    public LocalDateTime getDepartureAt() {
        return departureAt;
    }

    public String getArrivalIata() {
        return arrivalIata;
    }

    public LocalDateTime getArrivalAt() {
        return arrivalAt;
    }

    public Duration getDuration() {
        return duration;
    }

    public Boolean getBlacklistedInEu() {
        return blacklistedInEu;
    }

    public Integer getNumberOfStops() {
        return numberOfStops;
    }

    public BigDecimal getPrice() {
        return price;
    }
    // END: Getter

    // BEGIN: Builder
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private FlightOfferId flightOfferId;
        private String carrierCode;
        private String operatingCarrierCode;
        private String flightNumber;
        private String aircraftCode;
        private String departureIata;
        private LocalDateTime departureAt;
        private String arrivalIata;
        private LocalDateTime arrivalAt;
        private Duration duration;
        private Boolean blacklistedInEu;
        private Integer numberOfStops;
        private BigDecimal price;

        private Builder() {
        }

        public Builder id(FlightOfferId flightOfferId) {
            this.flightOfferId = flightOfferId;
            return this;
        }

        public Builder carrierCode(String carrierCode) {
            this.carrierCode = carrierCode;
            return this;
        }

        public Builder operatingCarrierCode(String operatingCarrierCode) {
            this.operatingCarrierCode = operatingCarrierCode;
            return this;
        }

        public Builder flightNumber(String flightNumber) {
            this.flightNumber = flightNumber;
            return this;
        }

        public Builder aircraftCode(String aircraftCode) {
            this.aircraftCode = aircraftCode;
            return this;
        }

        public Builder departureIata(String departureIata) {
            this.departureIata = departureIata;
            return this;
        }

        public Builder departureAt(LocalDateTime departureAt) {
            this.departureAt = departureAt;
            return this;
        }

        public Builder arrivalIata(String arrivalIata) {
            this.arrivalIata = arrivalIata;
            return this;
        }

        public Builder arrivalAt(LocalDateTime arrivalAt) {
            this.arrivalAt = arrivalAt;
            return this;
        }

        public Builder duration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Builder blacklistedInEu(Boolean blacklistedInEu) {
            this.blacklistedInEu = blacklistedInEu;
            return this;
        }

        public Builder numberOfStops(Integer numberOfStops) {
            this.numberOfStops = numberOfStops;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public FlightOffer build() {
            return new FlightOffer(this);
        }
    }
    // END: Builder

}
