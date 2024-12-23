package com.traveladvisor.carserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.AggregateRoot;
import com.traveladvisor.common.domain.vo.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * CarOffer 도메인의 Aggregate Root입니다.
 * 렌트 차량 오퍼 정보를 포함하는 핵심 엔터티입니다.
 */
public class CarOffer extends AggregateRoot<CarOfferId> {

    private final String vehicleCode;
    private final String category;
    private final String description;
    private final String imageUrl;
    private final String baggages;
    private final String seats;
    private final BigDecimal price;

    private BookingApproval bookingApproval;

    private CarOffer(Builder builder) {
        super.setId(builder.carOfferId);
        this.vehicleCode = builder.vehicleCode;
        this.category = builder.category;
        this.description = builder.description;
        this.imageUrl = builder.imageUrl;
        this.baggages = builder.baggages;
        this.seats = builder.seats;
        this.price = builder.price;
    }

    /**
     * 차량 예약서가 유효한지 검증합니다.
     *
     * @param failureMessages 검증 실패 메시지를 담을 리스트
     */
    public void validateCarOffer(List<String> failureMessages) {
        validateVehicleCode(failureMessages);
        validateCategory(failureMessages);
        validatePrice(failureMessages);

        // 그 외 차량 유효성 검증은 스킵합니다.
    }

    /**
     * Vehicle Code 검증
     */
    private void validateVehicleCode(List<String> failureMessages) {
        if (vehicleCode == null || vehicleCode.isEmpty()) {
            failureMessages.add("차량 코드(vehicle code)가 유효하지 않습니다.");
        }
    }

    /**
     * Category 검증
     */
    private void validateCategory(List<String> failureMessages) {
        if (category == null || category.isEmpty()) {
            failureMessages.add("카테고리(category)가 유효하지 않습니다.");
        }
    }

    /**
     * Price 검증
     */
    private void validatePrice(List<String> failureMessages) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            failureMessages.add("가격(price)은 0보다 커야 하며 null이 아니어야 합니다.");
        }
    }

    /**
     * 차량 예약 엔터티를 초기화 합니다.
     */
    public void initializeBookingApproval(BookingId bookingId, CarBookingApprovalStatus carBookingApprovalStatus) {
        this.bookingApproval = BookingApproval.builder()
                .bookingId(bookingId)
                .bookingApprovalId(new BookingApprovalId(UUID.randomUUID()))
                .carOfferId(this.getId())
                .status(carBookingApprovalStatus)
                .build();
    }

    // BEGIN: Getter
    public String getVehicleCode() {
        return vehicleCode;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBaggages() {
        return baggages;
    }

    public String getSeats() {
        return seats;
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
        private CarOfferId carOfferId;
        private String vehicleCode;
        private String category;
        private String description;
        private String imageUrl;
        private String baggages;
        private String seats;
        private BigDecimal price;

        private Builder() {}

        public Builder id(CarOfferId carOfferId) {
            this.carOfferId = carOfferId;
            return this;
        }

        public Builder vehicleCode(String vehicleCode) {
            this.vehicleCode = vehicleCode;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder baggages(String baggages) {
            this.baggages = baggages;
            return this;
        }

        public Builder seats(String seats) {
            this.seats = seats;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public CarOffer build() {
            return new CarOffer(this);
        }
    }
    // END: Builder

}
