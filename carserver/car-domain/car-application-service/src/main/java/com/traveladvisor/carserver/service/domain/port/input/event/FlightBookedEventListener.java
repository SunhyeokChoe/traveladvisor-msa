package com.traveladvisor.carserver.service.domain.port.input.event;

import com.traveladvisor.carserver.service.domain.dto.query.command.CarBookingCommand;

public interface CarBookedEventListener {

    /**
     * 차량 예약을 진행합니다.
     *
     * @param carBookingCommand
     */
    void processCarBooking(CarBookingCommand carBookingCommand);

    /**
     * 완료했던 차량 예약을 취소합니다.
     *
     * @param carBookingCommand
     */
    void compensateCarBooking(CarBookingCommand carBookingCommand);

}
