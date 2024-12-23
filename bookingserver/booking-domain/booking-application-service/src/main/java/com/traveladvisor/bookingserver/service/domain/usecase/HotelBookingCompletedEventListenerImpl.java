package com.traveladvisor.bookingserver.service.domain.usecase;

import com.traveladvisor.bookingserver.service.domain.dto.message.HotelBookingResponse;
import com.traveladvisor.bookingserver.service.domain.port.input.event.HotelBookingCompletedEventListener;
import com.traveladvisor.bookingserver.service.domain.usecase.saga.BookingHotelSagaAction;
import com.traveladvisor.hotelserver.service.domain.dto.command.HotelBookingCommand;
import com.traveladvisor.hotelserver.service.domain.port.input.event.BookingCreatedEventListener;
import com.traveladvisor.hotelserver.service.domain.usecase.command.CompleteHotelBookingHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class HotelBookingCompletedEventListenerImpl implements HotelBookingCompletedEventListener {

    private final BookingHotelSagaAction bookingHotelSagaAction;

    @Override
    public void processFlightBooking(HotelBookingResponse hotelBookingResponse) {
        bookingHotelSagaAction.process(hotelBookingResponse);
    }

    @Override
    public void compensateHotelBooking(HotelBookingResponse hotelBookingResponse) {
        bookingHotelSagaAction.compensate(hotelBookingResponse);
    }

}
