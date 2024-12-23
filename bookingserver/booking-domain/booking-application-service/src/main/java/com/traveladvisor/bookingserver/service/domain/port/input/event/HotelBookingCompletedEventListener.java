package com.traveladvisor.bookingserver.service.domain.port.input.event;

import com.traveladvisor.bookingserver.service.domain.dto.message.HotelBookingResponse;

public interface HotelBookingCompletedEventListener {

    /**
     * 항공권 예약을 진행합니다.
     *
     * @param hotelBookingResponse
     */
    void processFlightBooking(HotelBookingResponse hotelBookingResponse);

    /**
     * 완료했던 호텔 예약을 취소합니다.
     *
     * @param hotelBookingResponse
     */
    void compensateHotelBooking(HotelBookingResponse hotelBookingResponse);

}
