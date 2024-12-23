package com.traveladvisor.bookingserver.service.domain.event;

import com.traveladvisor.bookingserver.service.domain.entity.Booking;

import java.time.ZonedDateTime;

/**
 * 예약서 생성 완료 이벤트입니다.
 */
public class BookingCreatedEvent extends BookingEvent {

    public BookingCreatedEvent(Booking booking, ZonedDateTime createdAt) {
        super(booking, createdAt);
    }

}
