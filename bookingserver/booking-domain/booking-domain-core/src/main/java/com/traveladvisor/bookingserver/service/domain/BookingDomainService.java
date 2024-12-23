package com.traveladvisor.bookingserver.service.domain;

import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.bookingserver.service.domain.event.BookingCreatedEvent;
import com.traveladvisor.bookingserver.service.domain.event.FlightBookedEvent;
import com.traveladvisor.bookingserver.service.domain.event.HotelBookedEvent;

public interface BookingDomainService {

    /**
     * 예약서를 초기화 합니다.
     *
     * @param booking
     * @return
     */
    BookingCreatedEvent initializeBooking(Booking booking);

    /**
     * 예약서의 예약 상태를 호텔 예약 완료 상태로 변경합니다.
     *
     * @param booking
     * @return
     */
    HotelBookedEvent markAsHotelBooked(Booking booking);

    /**
     * 예약서의 예약 상태를 항공권 예약 완료 상태로 변경합니다.
     *
     * @param booking
     * @return
     */
    FlightBookedEvent markAsFlightBooked(Booking booking);

}
