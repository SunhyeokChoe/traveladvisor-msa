package com.traveladvisor.hotelserver.service.domain.usecase;

import com.traveladvisor.hotelserver.service.domain.dto.command.HotelBookingCommand;
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
    public void processHotelBooking(HotelBookingCommand hotelBookingCommand) {
        completeHotelBookingHelper.completeHotelBooking(hotelBookingCommand);
    }

    @Override
    public void compensateHotelBooking(HotelBookingCommand hotelBookingCommand) {
        completeHotelBookingHelper.cancelHotelBooking(hotelBookingCommand);
    }

}
