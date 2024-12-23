package com.traveladvisor.bookingserver.service.domain.event;

import com.traveladvisor.bookingserver.service.domain.entity.Booking;

import java.time.ZonedDateTime;

/**
 * 항공권 예약 완료 이벤트입니다.
 */
public class FlightBookedEvent extends BookingEvent {

    public FlightBookedEvent(Booking booking, ZonedDateTime createdAt) {
        super(booking, createdAt);
    }

}
