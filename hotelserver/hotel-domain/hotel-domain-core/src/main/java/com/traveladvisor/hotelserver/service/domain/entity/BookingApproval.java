package com.traveladvisor.hotelserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.DomainEntity;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.HotelBookingStatus;
import com.traveladvisor.common.domain.vo.HotelOfferId;

public class BookingApproval extends DomainEntity<BookingApprovalId> {

    private final BookingId bookingId;
    private final HotelOfferId hotelOffersId;
    private final HotelBookingStatus status;

    private BookingApproval(Builder builder) {
        super.setId(builder.bookingApprovalId);
        this.bookingId = builder.bookingId;
        this.hotelOffersId = builder.hotelOffersId;
        this.status = builder.status;
    }

    // BEGIN: Getter
    public BookingId getBookingId() {
        return bookingId;
    }

    public HotelOfferId getHotelOffersId() {
        return hotelOffersId;
    }

    public HotelBookingStatus getStatus() {
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
        private HotelOfferId hotelOffersId;
        private HotelBookingStatus status;

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

        public Builder hotelOffersId(HotelOfferId val) {
            hotelOffersId = val;
            return this;
        }

        public Builder status(HotelBookingStatus val) {
            status = val;
            return this;
        }

        public BookingApproval build() {
            return new BookingApproval(this);
        }
    }
    // END: Builder

}
