package com.traveladvisor.carserver.service.domain.event;

import com.traveladvisor.carserver.service.domain.entity.BookingApproval;
import com.traveladvisor.common.domain.event.DomainEvent;

import java.time.ZonedDateTime;
import java.util.List;

public abstract class CarBookingEvent implements DomainEvent<BookingApproval> {

    private final BookingApproval bookingApproval;
    private final List<String> failureMessages;
    private final ZonedDateTime createdAt;

    protected CarBookingEvent(BookingApproval bookingApproval,
                              List<String> failureMessages,
                              ZonedDateTime createdAt) {
        this.bookingApproval = bookingApproval;
        this.failureMessages = failureMessages;
        this.createdAt = createdAt;
    }

    public BookingApproval getBookingApproval() {
        return bookingApproval;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

}
