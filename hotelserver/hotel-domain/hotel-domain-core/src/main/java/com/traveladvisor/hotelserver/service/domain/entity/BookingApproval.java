package com.traveladvisor.hotelserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.DomainEntity;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.HotelBookingApprovalStatus;
import com.traveladvisor.common.domain.vo.HotelOfferId;

public class BookingApproval extends DomainEntity<BookingApprovalId> {

    private final BookingId bookingId;
    private final HotelOfferId hotelOfferId;
    private final HotelBookingApprovalStatus status;

    private BookingApproval(Builder builder) {
        super.setId(builder.bookingApprovalId);
        this.bookingId = builder.bookingId;
        this.hotelOfferId = builder.hotelOfferId;
        this.status = builder.status;
    }

    // BEGIN: Getter
    public BookingId getBookingId() {
        return bookingId;
    }

    public HotelOfferId getHotelOfferId() {
        return hotelOfferId;
    }

    public HotelBookingApprovalStatus getStatus() {
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
        private HotelOfferId hotelOfferId;
        private HotelBookingApprovalStatus status;

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

        public Builder hotelOfferId(HotelOfferId val) {
            hotelOfferId = val;
            return this;
        }

        public Builder status(HotelBookingApprovalStatus val) {
            status = val;
            return this;
        }

        public BookingApproval build() {
            return new BookingApproval(this);
        }
    }
    // END: Builder

}
