package com.traveladvisor.bookingserver.service.domain.usecase;

import com.traveladvisor.bookingserver.service.domain.dto.message.FlightBookingResponse;
import com.traveladvisor.bookingserver.service.domain.port.input.event.FlightBookingCompletedEventListener;
import com.traveladvisor.bookingserver.service.domain.usecase.saga.BookingFlightSagaAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@RequiredArgsConstructor
@Validated
@Service
public class FlightBookingCompletedEventListenerImpl implements FlightBookingCompletedEventListener {

    private final BookingFlightSagaAction bookingFlightSagaAction;

    @Override
    public void processFlightBooking(FlightBookingResponse flightBookingResponse) {
        bookingFlightSagaAction.process(flightBookingResponse);
    }

    @Override
    public void compensateFlightBooking(FlightBookingResponse flightBookingResponse) {
        bookingFlightSagaAction.compensate(flightBookingResponse);
    }

}
