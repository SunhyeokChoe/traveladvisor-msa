package com.traveladvisor.bookingserver.service.domain.event;

import com.traveladvisor.bookingserver.service.domain.entity.Booking;

import java.time.ZonedDateTime;

/**
 * 호텔 예약 완료 이벤트입니다.
 */
public class HotelBookedEvent extends BookingEvent {

    public HotelBookedEvent(Booking booking, ZonedDateTime createdAt) {
        super(booking, createdAt);
    }

}
