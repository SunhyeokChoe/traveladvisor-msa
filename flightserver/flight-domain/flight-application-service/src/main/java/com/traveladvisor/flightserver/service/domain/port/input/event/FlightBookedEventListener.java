package com.traveladvisor.flightserver.service.domain.port.input.event;

import com.traveladvisor.flightserver.service.domain.dto.command.FlightBookingCommand;

public interface FlightBookedEventListener {

    /**
     * 항공권 예약을 진행합니다.
     *
     * @param flightBookingCommand
     */
    void processFlightBooking(FlightBookingCommand flightBookingCommand);

    /**
     * 완료했던 항공권 예약을 취소합니다.
     *
     * @param flightBookingCommand
     */
    void compensateFlightBooking(FlightBookingCommand flightBookingCommand);

}
