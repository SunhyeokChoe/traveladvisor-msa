package com.traveladvisor.bookingserver.service.domain.port.input.event;

import com.traveladvisor.bookingserver.service.domain.dto.message.CarBookingResponse;
import jakarta.validation.Valid;

public interface CarBookingCompletedEventListener {

    /**
     * 차량 예약을 완료 상태로 전환합니다.
     *
     * @param carBookingResponse
     */
    void processCarBooking(@Valid CarBookingResponse carBookingResponse);

    /**
     * 예약서의 예약 상태를 예약 실패 상태로 전환 후 항공권 예약을 취소하기 위해 Flight Outbox에 이벤트를 기록합니다.
     *
     * @param carBookingResponse
     */
    void compensateCarBooking(@Valid CarBookingResponse carBookingResponse);

}
