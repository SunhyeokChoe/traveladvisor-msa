package com.traveladvisor.carserver.service.domain.event;

import com.traveladvisor.carserver.service.domain.entity.BookingApproval;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * 차량 예약 반려 이벤트 입니다.
 */
public class CarBookingRejectedEvent extends CarBookingEvent {

    public CarBookingRejectedEvent(BookingApproval bookingApproval, List<String> failureMessages, ZonedDateTime createdAt) {
        super(bookingApproval, failureMessages, createdAt);
    }

}
