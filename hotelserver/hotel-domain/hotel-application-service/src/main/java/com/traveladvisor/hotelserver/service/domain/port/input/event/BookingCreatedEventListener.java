package com.traveladvisor.hotelserver.service.domain.port.input.event;

import com.traveladvisor.hotelserver.service.domain.dto.command.CompleteHotelBookingCommand;

public interface BookingCreatedEventListener {

    /**
     * 호텔 예약을 진행합니다.
     *
     * @param completeHotelBookingCommand
     */
    void completeHotelBooking(CompleteHotelBookingCommand completeHotelBookingCommand);

    /**
     * 완료했던 호텔 예약을 취소합니다.
     *
     * @param completeHotelBookingCommand
     */
    void cancelHotelBooking(CompleteHotelBookingCommand completeHotelBookingCommand);

}
