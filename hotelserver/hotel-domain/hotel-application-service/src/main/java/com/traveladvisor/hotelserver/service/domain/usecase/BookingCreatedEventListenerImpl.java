package com.traveladvisor.hotelserver.service.domain.usecase;

import com.traveladvisor.hotelserver.service.domain.dto.command.CompleteHotelBookingCommand;
import com.traveladvisor.hotelserver.service.domain.port.input.event.BookingCreatedEventListener;
import com.traveladvisor.hotelserver.service.domain.usecase.command.CompleteHotelBookingHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookingCreatedEventListenerImpl implements BookingCreatedEventListener {

    private final CompleteHotelBookingHelper completeHotelBookingHelper;

    @Override
    public void completeHotelBooking(CompleteHotelBookingCommand completeHotelBookingCommand) {
        completeHotelBookingHelper.completeHotelBooking(completeHotelBookingCommand);
    }

    @Override
    public void cancelHotelBooking(CompleteHotelBookingCommand completeHotelBookingCommand) {
        completeHotelBookingHelper.cancelHotelBooking(completeHotelBookingCommand);
    }

}
