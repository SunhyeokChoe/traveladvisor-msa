package com.traveladvisor.carserver.service.domain.usecase;

import com.traveladvisor.carserver.service.domain.dto.query.command.CarBookingCommand;
import com.traveladvisor.carserver.service.domain.port.input.event.CarBookedEventListener;
import com.traveladvisor.carserver.service.domain.usecase.command.CompleteCarBookingHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CarBookedEventListenerImpl implements CarBookedEventListener {

    private final CompleteCarBookingHelper completeCarBookingHelper;

    @Override
    public void processCarBooking(CarBookingCommand carBookingCommand) {
        completeCarBookingHelper.completeCarBooking(carBookingCommand);
    }

    @Override
    public void compensateCarBooking(CarBookingCommand carBookingCommand) {
        completeCarBookingHelper.cancelCarBooking(carBookingCommand);
    }

}
