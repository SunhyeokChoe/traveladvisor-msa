package com.traveladvisor.hotelserver.service.domain.port.input.event;

import com.traveladvisor.hotelserver.service.domain.dto.command.HotelBookingCommand;

public interface BookingCreatedEventListener {

    /**
     * 호텔 예약을 진행합니다.
     *
     * @param hotelBookingCommand
     */
    void processHotelBooking(HotelBookingCommand hotelBookingCommand);

    /**
     * 완료했던 호텔 예약을 취소합니다.
     *
     * @param hotelBookingCommand
     */
    void compensateHotelBooking(HotelBookingCommand hotelBookingCommand);

}
