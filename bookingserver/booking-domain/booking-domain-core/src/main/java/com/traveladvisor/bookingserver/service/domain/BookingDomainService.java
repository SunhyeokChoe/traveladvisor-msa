package com.traveladvisor.bookingserver.service.domain;

import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.bookingserver.service.domain.event.BookingCancelledEvent;
import com.traveladvisor.bookingserver.service.domain.event.BookingCreatedEvent;
import com.traveladvisor.bookingserver.service.domain.event.FlightBookedEvent;
import com.traveladvisor.bookingserver.service.domain.event.HotelBookedEvent;

import java.util.List;

public interface BookingDomainService {

    /**
     * 예약서를 초기화 합니다.
     *
     * @param booking 예약서
     * @return
     */
    BookingCreatedEvent initializeBooking(Booking booking);

    /**
     * 예약서의 예약 상태를 호텔 예약 완료 상태로 변경합니다.
     *
     * @param booking 예약서
     * @return
     */
    HotelBookedEvent markAsHotelBooked(Booking booking);

    /**
     * 예약서의 예약 상태를 항공권 예약 완료 상태로 변경합니다.
     *
     * @param booking 예약서
     * @return
     */
    FlightBookedEvent markAsFlightBooked(Booking booking);

    /**
     * 예약서에 실패 메시지 목록을 등록하고 항공권 예약 실패 상태로 변경합니다.
     *
     * @param booking         예약서
     * @param failureMessages 예약 실패 메시지 목록
     * @return
     */
    BookingCancelledEvent initializeBookingCancelling(Booking booking, List<String> failureMessages);

    /**
     * 예약서에 실패 메시지 목록을 등록하고 예약 실패 상태로 변경합니다.
     *
     * @param booking         예약서
     * @param failureMessages 예약 실패 메시지 목록
     * @return
     */
    void cancelBooking(Booking booking, List<String> failureMessages);

}
