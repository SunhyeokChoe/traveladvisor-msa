package com.traveladvisor.bookingserver.service.domain.usecase;

import com.traveladvisor.bookingserver.service.domain.dto.message.CarBookingResponse;
import com.traveladvisor.bookingserver.service.domain.port.input.event.CarBookingCompletedEventListener;
import com.traveladvisor.bookingserver.service.domain.usecase.saga.BookingCarSagaAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@RequiredArgsConstructor
@Validated
@Service
public class CarBookingCompletedEventListenerImpl implements CarBookingCompletedEventListener {

    private final BookingCarSagaAction bookingCarSagaAction;

    @Override
    public void processCarBooking(CarBookingResponse carBookingResponse) {
        bookingCarSagaAction.process(carBookingResponse);
    }

    @Override
    public void compensateCarBooking(CarBookingResponse carBookingResponse) {
        bookingCarSagaAction.compensate(carBookingResponse);
    }

}
