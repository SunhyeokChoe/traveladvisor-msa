package com.traveladvisor.bookingserver.service.domain.port.input.event;

import com.traveladvisor.bookingserver.service.domain.dto.message.FlightBookingResponse;

public interface FlightBookingCompletedEventListener {

    /**
     * 항공권 예약 완료로 상태 전환 후 차량 예약을 진행하기 위해 Car Outbox에 이벤트를 저장합니다.
     *
     * @param flightBookingResponse
     */
    void processCarBooking(FlightBookingResponse flightBookingResponse);

    /**
     * 예약서의 예약 상태를 예약 실패 상태로 전환 후 항공권 예약을 취소하기 위해 Flight Outbox에 이벤트를 기록합니다.
     *
     * @param flightBookingResponse
     */
    void compensateFlightBooking(FlightBookingResponse flightBookingResponse);

}
