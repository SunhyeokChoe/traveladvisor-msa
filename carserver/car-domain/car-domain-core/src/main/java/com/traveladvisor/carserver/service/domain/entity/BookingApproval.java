package com.traveladvisor.carserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.DomainEntity;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.CarBookingApprovalStatus;
import com.traveladvisor.common.domain.vo.CarOfferId;

public class BookingApproval extends DomainEntity<BookingApprovalId> {

    private final BookingId bookingId;
    private final CarOfferId carOfferId;
    private final CarBookingApprovalStatus status;

    private BookingApproval(Builder builder) {
        super.setId(builder.bookingApprovalId);
        this.bookingId = builder.bookingId;
        this.carOfferId = builder.carOfferId;
        this.status = builder.status;
    }

    // BEGIN: Getter
    public BookingId getBookingId() {
        return bookingId;
    }

    public CarOfferId getCarOfferId() {
        return carOfferId;
    }

    public CarBookingApprovalStatus getStatus() {
        return status;
    }
    // END: Getter

    // BEGIN: Builder
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private BookingApprovalId bookingApprovalId;
        private BookingId bookingId;
        private CarOfferId carOfferId;
        private CarBookingApprovalStatus status;

        private Builder() {
        }

        public Builder bookingApprovalId(BookingApprovalId val) {
            bookingApprovalId = val;
            return this;
        }

        public Builder bookingId(BookingId val) {
            bookingId = val;
            return this;
        }

        public Builder carOfferId(CarOfferId val) {
            carOfferId = val;
            return this;
        }

        public Builder status(CarBookingApprovalStatus val) {
            status = val;
            return this;
        }

        public BookingApproval build() {
            return new BookingApproval(this);
        }
    }
    // END: Builder

}
