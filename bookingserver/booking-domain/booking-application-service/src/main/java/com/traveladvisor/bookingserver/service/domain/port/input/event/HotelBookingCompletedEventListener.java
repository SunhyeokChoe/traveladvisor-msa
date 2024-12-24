package com.traveladvisor.bookingserver.service.domain.port.input.event;

import com.traveladvisor.bookingserver.service.domain.dto.message.HotelBookingResponse;
import jakarta.validation.Valid;

public interface HotelBookingCompletedEventListener {

    /**
     * 호텔 예약 완료로 상태 전환 후 항공권 예약을 진행하기 위해 Flight Outbox에 이벤트를 저장합니다.
     *
     * @param hotelBookingResponse
     */
    void processHotelBooking(@Valid HotelBookingResponse hotelBookingResponse);

    /**
     * 예약서의 예약 상태를 예약 실패 상태로 전환합니다.
     *
     * @param hotelBookingResponse
     */
    void compensateHotelBooking(@Valid HotelBookingResponse hotelBookingResponse);

}
