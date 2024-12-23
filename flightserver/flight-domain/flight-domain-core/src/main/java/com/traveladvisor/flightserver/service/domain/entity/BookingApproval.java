package com.traveladvisor.flightserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.DomainEntity;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.FlightBookingApprovalStatus;
import com.traveladvisor.common.domain.vo.FlightOfferId;

public class BookingApproval extends DomainEntity<BookingApprovalId> {

    private final BookingId bookingId;
    private final FlightOfferId flightOfferId;
    private final FlightBookingApprovalStatus status;

    private BookingApproval(Builder builder) {
        super.setId(builder.bookingApprovalId);
        this.bookingId = builder.bookingId;
        this.flightOfferId = builder.flightOfferId;
        this.status = builder.status;
    }

    // BEGIN: Getter
    public BookingId getBookingId() {
        return bookingId;
    }

    public FlightOfferId getFlightOfferId() {
        return flightOfferId;
    }

    public FlightBookingApprovalStatus getStatus() {
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
        private FlightOfferId flightOfferId;
        private FlightBookingApprovalStatus status;

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

        public Builder flightOfferId(FlightOfferId val) {
            flightOfferId = val;
            return this;
        }

        public Builder status(FlightBookingApprovalStatus val) {
            status = val;
            return this;
        }

        public BookingApproval build() {
            return new BookingApproval(this);
        }
    }
    // END: Builder

}
