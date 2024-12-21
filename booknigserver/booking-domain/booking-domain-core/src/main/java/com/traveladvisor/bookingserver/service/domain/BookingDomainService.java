package com.traveladvisor.bookingserver.service.domain;

import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.bookingserver.service.domain.event.BookingCreatedEvent;

public interface BookingDomainService {

    BookingCreatedEvent initializeBooking(Booking booking);

}
