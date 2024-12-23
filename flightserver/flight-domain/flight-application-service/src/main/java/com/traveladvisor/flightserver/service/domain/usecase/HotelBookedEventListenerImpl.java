package com.traveladvisor.flightserver.service.domain.usecase;

import com.traveladvisor.flightserver.service.domain.dto.command.FlightBookingCommand;
import com.traveladvisor.flightserver.service.domain.port.input.event.HotelBookedEventListener;
import com.traveladvisor.flightserver.service.domain.usecase.command.CompleteFlightBookingHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class HotelBookedEventListenerImpl implements HotelBookedEventListener {

    private final CompleteFlightBookingHelper completeFlightBookingHelper;

    @Override
    public void processFlightBooking(FlightBookingCommand flightBookingCommand) {
        completeFlightBookingHelper.completeFlightBooking(flightBookingCommand);
    }

    @Override
    public void compensateFlightBooking(FlightBookingCommand flightBookingCommand) {
        completeFlightBookingHelper.cancelFlightBooking(flightBookingCommand);
    }

}
