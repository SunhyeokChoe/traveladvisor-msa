package com.traveladvisor.flightserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.AggregateRoot;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.FlightBookingApprovalStatus;
import com.traveladvisor.common.domain.vo.FlightOfferId;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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

    private BookingApproval bookingApproval;

    /**
     * 항공권 예약서가 유효한지 검증합니다.
     *
     * @param failureMessages 검증 실패 메시지를 담을 리스트
     */
    public void validateFlightOffer(List<String> failureMessages) {
        validateDepartureAndArrival(failureMessages);
        validateDuration(failureMessages);
        validatePrice(failureMessages);
        validateBlacklistedCarrier(failureMessages);
        // 그 외 항공권 유효성 검증은 스킵합니다.
    }

    private void validateDepartureAndArrival(List<String> failureMessages) {
        if (departureIata == null || departureIata.isBlank()) {
            failureMessages.add("출발 공항 코드가 유효하지 않습니다.");
        }
        if (arrivalIata == null || arrivalIata.isBlank()) {
            failureMessages.add("도착 공항 코드가 유효하지 않습니다.");
        }
        if (departureAt == null || arrivalAt == null || departureAt.isAfter(arrivalAt)) {
            failureMessages.add("출발 및 도착 시간이 유효하지 않습니다.");
        }
    }

    private void validateDuration(List<String> failureMessages) {
        if (duration == null || duration.isNegative() || duration.isZero()) {
            failureMessages.add("비행 시간이 유효하지 않습니다.");
        }
    }

    private void validatePrice(List<String> failureMessages) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            failureMessages.add("항공권 가격이 유효하지 않습니다.");
        }
    }

    private void validateBlacklistedCarrier(List<String> failureMessages) {
        if (blacklistedInEu != null && blacklistedInEu) {
            failureMessages.add("이 항공사는 EU 블랙리스트에 포함되어 있습니다.");
        }
    }

    /**
     * 항공권 예약 엔터티를 초기화 합니다.
     */
    public void initializeBookingApproval(BookingId bookingId, FlightBookingApprovalStatus flightBookingApprovalStatus) {
        this.bookingApproval = BookingApproval.builder()
                .bookingId(bookingId)
                .bookingApprovalId(new BookingApprovalId(UUID.randomUUID()))
                .flightOfferId(this.getId())
                .status(flightBookingApprovalStatus)
                .build();
    }

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

    public BookingApproval getBookingApproval() {
        return bookingApproval;
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
