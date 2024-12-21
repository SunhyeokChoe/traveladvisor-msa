package com.traveladvisor.carserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.AggregateRoot;
import com.traveladvisor.common.domain.vo.CarOfferId;

import java.math.BigDecimal;

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
