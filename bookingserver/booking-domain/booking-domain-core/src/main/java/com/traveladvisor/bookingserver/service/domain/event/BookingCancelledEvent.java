package com.traveladvisor.bookingserver.service.domain.event;

import com.traveladvisor.bookingserver.service.domain.entity.Booking;

import java.time.ZonedDateTime;

/**
 * 예약서 취소 완료 이벤트입니다.
 */
public class BookingCancelledEvent extends BookingEvent {

    public BookingCancelledEvent(Booking booking, ZonedDateTime createdAt) {
        super(booking, createdAt);
    }

}
