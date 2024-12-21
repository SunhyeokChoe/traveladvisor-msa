package com.traveladvisor.bookingserver.service.domain.event;

import com.traveladvisor.bookingserver.service.domain.entity.Booking;

import java.time.ZonedDateTime;

public class BookingCreatedEvent extends BookingEvent {

    public BookingCreatedEvent(Booking booking, ZonedDateTime createdAt) {
        super(booking, createdAt);
    }

}
