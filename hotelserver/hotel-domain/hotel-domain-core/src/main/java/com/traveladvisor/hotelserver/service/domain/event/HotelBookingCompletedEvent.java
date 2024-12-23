package com.traveladvisor.hotelserver.service.domain.event;

import com.traveladvisor.hotelserver.service.domain.entity.BookingApproval;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * 호텔 예약 완료 이벤트 입니다.
 */
public class HotelBookingCompletedEvent extends HotelBookingEvent {

    public HotelBookingCompletedEvent(BookingApproval bookingApproval, List<String> failureMessages, ZonedDateTime createdAt) {
        super(bookingApproval, failureMessages, createdAt);
    }

}
