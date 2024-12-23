package com.traveladvisor.bookingserver.service.domain.usecase;

import com.traveladvisor.bookingserver.service.domain.dto.message.FlightBookingResponse;
import com.traveladvisor.bookingserver.service.domain.port.input.event.FlightBookingCompletedEventListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class FlightBookingCompletedEventListenerImpl implements FlightBookingCompletedEventListener {

    private final BookingFlightSagaAction bookingFlightSagaAction;

    @Override
    public void processCarBooking(FlightBookingResponse flightBookingResponse) {
        bookingFlightSagaAction.process(flightBookingResponse);
    }

    @Override
    public void compensateFlightBooking(FlightBookingResponse flightBookingResponse) {
        bookingFlightSagaAction.compensate(flightBookingResponse);
    }

}
