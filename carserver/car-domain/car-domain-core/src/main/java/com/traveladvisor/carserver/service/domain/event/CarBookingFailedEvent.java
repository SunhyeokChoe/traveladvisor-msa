package com.traveladvisor.carserver.service.domain.event;

import com.traveladvisor.carserver.service.domain.entity.BookingApproval;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * 차량 예약 실패 이벤트 입니다.
 */
public class CarBookingFailedEvent extends CarBookingEvent {

    public CarBookingFailedEvent(BookingApproval bookingApproval, List<String> failureMessages, ZonedDateTime createdAt) {
        super(bookingApproval, failureMessages, createdAt);
    }

}
