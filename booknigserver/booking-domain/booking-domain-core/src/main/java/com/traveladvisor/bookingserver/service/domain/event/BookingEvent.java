package com.traveladvisor.bookingserver.service.domain.event;

import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.common.domain.event.DomainEvent;

import java.time.ZonedDateTime;

public abstract class BookingEvent implements DomainEvent<Booking> {

    private final Booking booking;
    private final ZonedDateTime createdAt;

    public BookingEvent(Booking booking, ZonedDateTime createdAt) {
        this.booking = booking;
        this.createdAt = createdAt;
    }

    public Booking getBooking() {
        return booking;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
    
}
