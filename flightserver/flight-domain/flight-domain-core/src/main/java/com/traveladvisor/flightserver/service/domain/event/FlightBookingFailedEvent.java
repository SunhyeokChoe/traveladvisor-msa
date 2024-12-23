package com.traveladvisor.flightserver.service.domain.event;

import com.traveladvisor.flightserver.service.domain.entity.BookingApproval;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * 항공권 예약 실패 이벤트 입니다.
 */
public class FlightBookingFailedEvent extends FlightBookingEvent {

    public FlightBookingFailedEvent(BookingApproval bookingApproval, List<String> failureMessages, ZonedDateTime createdAt) {
        super(bookingApproval, failureMessages, createdAt);
    }

}
