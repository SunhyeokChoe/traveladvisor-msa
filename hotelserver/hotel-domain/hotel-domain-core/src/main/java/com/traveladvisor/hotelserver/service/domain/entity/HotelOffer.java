package com.traveladvisor.hotelserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.AggregateRoot;
import com.traveladvisor.common.domain.vo.*;
import com.traveladvisor.hotelserver.service.domain.exception.HotelDomainCoreException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * HotelOffer 도메인의 Aggregate Root입니다.
 * 호텔 예약 가능 오퍼 정보를 포함하는 핵심 엔터티입니다.
 */
public class HotelOffer extends AggregateRoot<HotelOfferId> {

    // 최소 예약 금액
    private final static BigDecimal MINIMUM_BOOKING_PRICE = BigDecimal.valueOf(10000);

    private final HotelId hotelId;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final String rateCode;
    private final String rateFamilyCode;
    private final String rateFamilyType;
    private final String roomType;
    private final String roomCategory;
    private final Integer roomBeds;
    private final String roomBedType;
    private final String roomDescriptionText;
    private final String roomDescriptionLang;
    private final Integer guestsAdults;
    private final String priceCurrency;
    private final BigDecimal priceBase;
    private final BigDecimal priceTotal;
    private final String priceVariations;
    private final String cancellationPolicies;
    private final String paymentType;

    private BookingApproval bookingApproval;

    /**
     * 호텔 객실 예약서가 유효한지 검증합니다.
     *
     * @param failureMessages
     */
    public void validateHotelOffer(List<String> failureMessages) {
        validateCheckInAndOutDate();
        validatePriceTotal();
        validateRoomBeds(failureMessages);

        // 성인과 아이 수, 침대 수, 객실 ID 매칭 등 호텔 객실 예약에 필요한 비즈니스 니즈를 충족하는지 모두 검증해야 합니다.
        // .....생략.....
    }

    /**
     * 체크인/아웃 기간이 유효한지 검증합니다.
     */
    private void validateCheckInAndOutDate() {
        // checkInDate, checkOutDate 가 서버 시간 기준으로 유효한지 검증합니다.
        // 유효하다고 가정합니다.
    }

    /**
     * 객실 내 침대 수가 유효한지 검증합니다.
     *
     * @param failureMessages
     */
    private void validateRoomBeds(List<String> failureMessages) {
        if (roomBeds == null || roomBeds <= 0) {
            failureMessages.add("객실 수가 비정상적입니다. roomBeds: " + roomBeds);
        }
    }

    /**
     * 총 예약 금액이 최소 예약 금액을 충족하는지 검증합니다.
     */
    private void validatePriceTotal() {
        if (priceTotal == null || priceTotal.compareTo(MINIMUM_BOOKING_PRICE) < 0) {
            throw new HotelDomainCoreException("총 예약 금액은 최소 예약 금액 보다 커야 합니다.");
        }
    }

    /**
     * 호텔 예약 엔터티를 초기화 합니다.
     */
    public void initializeBookingApproval(BookingId bookingId, HotelBookingApprovalStatus hotelBookingApprovalStatus) {
        this.bookingApproval = BookingApproval.builder()
                .bookingId(bookingId)
                .bookingApprovalId(new BookingApprovalId(UUID.randomUUID()))
                .hotelOfferId(this.getId())
                .status(hotelBookingApprovalStatus)
                .build();
    }

    private HotelOffer(Builder builder) {
        super.setId(builder.hotelOfferId);
        this.hotelId = builder.hotelId;
        this.checkInDate = builder.checkInDate;
        this.checkOutDate = builder.checkOutDate;
        this.rateCode = builder.rateCode;
        this.rateFamilyCode = builder.rateFamilyCode;
        this.rateFamilyType = builder.rateFamilyType;
        this.roomType = builder.roomType;
        this.roomCategory = builder.roomCategory;
        this.roomBeds = builder.roomBeds;
        this.roomBedType = builder.roomBedType;
        this.roomDescriptionText = builder.roomDescriptionText;
        this.roomDescriptionLang = builder.roomDescriptionLang;
        this.guestsAdults = builder.guestsAdults;
        this.priceCurrency = builder.priceCurrency;
        this.priceBase = builder.priceBase;
        this.priceTotal = builder.priceTotal;
        this.priceVariations = builder.priceVariations;
        this.cancellationPolicies = builder.cancellationPolicies;
        this.paymentType = builder.paymentType;
        this.bookingApproval = builder.bookingApproval;
    }

    // BEGIN: Getter
    public HotelId getHotelId() {
        return hotelId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public String getRateCode() {
        return rateCode;
    }

    public String getRateFamilyCode() {
        return rateFamilyCode;
    }

    public String getRateFamilyType() {
        return rateFamilyType;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomCategory() {
        return roomCategory;
    }

    public Integer getRoomBeds() {
        return roomBeds;
    }

    public String getRoomBedType() {
        return roomBedType;
    }

    public String getRoomDescriptionText() {
        return roomDescriptionText;
    }

    public String getRoomDescriptionLang() {
        return roomDescriptionLang;
    }

    public Integer getGuestsAdults() {
        return guestsAdults;
    }

    public String getPriceCurrency() {
        return priceCurrency;
    }

    public BigDecimal getPriceBase() {
        return priceBase;
    }

    public BigDecimal getPriceTotal() {
        return priceTotal;
    }

    public String getPriceVariations() {
        return priceVariations;
    }

    public String getCancellationPolicies() {
        return cancellationPolicies;
    }

    public String getPaymentType() {
        return paymentType;
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
        private HotelOfferId hotelOfferId;
        private HotelId hotelId;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private String rateCode;
        private String rateFamilyCode;
        private String rateFamilyType;
        private String roomType;
        private String roomCategory;
        private Integer roomBeds;
        private String roomBedType;
        private String roomDescriptionText;
        private String roomDescriptionLang;
        private Integer guestsAdults;
        private String priceCurrency;
        private BigDecimal priceBase;
        private BigDecimal priceTotal;
        private String priceVariations;
        private String cancellationPolicies;
        private String paymentType;
        private BookingApproval bookingApproval;

        private Builder() {
        }

        public Builder id(HotelOfferId hotelOfferId) {
            this.hotelOfferId = hotelOfferId;
            return this;
        }

        public Builder hotelId(HotelId hotelId) {
            this.hotelId = hotelId;
            return this;
        }

        public Builder checkInDate(LocalDate checkInDate) {
            this.checkInDate = checkInDate;
            return this;
        }

        public Builder checkOutDate(LocalDate checkOutDate) {
            this.checkOutDate = checkOutDate;
            return this;
        }

        public Builder rateCode(String rateCode) {
            this.rateCode = rateCode;
            return this;
        }

        public Builder rateFamilyCode(String rateFamilyCode) {
            this.rateFamilyCode = rateFamilyCode;
            return this;
        }

        public Builder rateFamilyType(String rateFamilyType) {
            this.rateFamilyType = rateFamilyType;
            return this;
        }

        public Builder roomType(String roomType) {
            this.roomType = roomType;
            return this;
        }

        public Builder roomCategory(String roomCategory) {
            this.roomCategory = roomCategory;
            return this;
        }

        public Builder roomBeds(Integer roomBeds) {
            this.roomBeds = roomBeds;
            return this;
        }

        public Builder roomBedType(String roomBedType) {
            this.roomBedType = roomBedType;
            return this;
        }

        public Builder roomDescriptionText(String roomDescriptionText) {
            this.roomDescriptionText = roomDescriptionText;
            return this;
        }

        public Builder roomDescriptionLang(String roomDescriptionLang) {
            this.roomDescriptionLang = roomDescriptionLang;
            return this;
        }

        public Builder guestsAdults(Integer guestsAdults) {
            this.guestsAdults = guestsAdults;
            return this;
        }

        public Builder priceCurrency(String priceCurrency) {
            this.priceCurrency = priceCurrency;
            return this;
        }

        public Builder priceBase(BigDecimal priceBase) {
            this.priceBase = priceBase;
            return this;
        }

        public Builder priceTotal(BigDecimal priceTotal) {
            this.priceTotal = priceTotal;
            return this;
        }

        public Builder priceVariations(String priceVariations) {
            this.priceVariations = priceVariations;
            return this;
        }

        public Builder cancellationPolicies(String cancellationPolicies) {
            this.cancellationPolicies = cancellationPolicies;
            return this;
        }

        public Builder paymentType(String paymentType) {
            this.paymentType = paymentType;
            return this;
        }

        public Builder getBookingApproval(BookingApproval bookingApproval) {
            this.bookingApproval = bookingApproval;
            return this;
        }

        public HotelOffer build() {
            return new HotelOffer(this);
        }
    }
    // END: Builder

}
