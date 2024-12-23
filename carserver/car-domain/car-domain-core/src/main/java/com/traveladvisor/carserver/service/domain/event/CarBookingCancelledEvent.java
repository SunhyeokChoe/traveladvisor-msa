package com.traveladvisor.carserver.service.domain.event;

import com.traveladvisor.carserver.service.domain.entity.BookingApproval;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * 차량 예약 취소 완료 이벤트 입니다.
 */
public class CarBookingCancelledEvent extends CarBookingEvent {

    public CarBookingCancelledEvent(BookingApproval bookingApproval, List<String> failureMessages, ZonedDateTime createdAt) {
        super(bookingApproval, failureMessages, createdAt);
    }

}
