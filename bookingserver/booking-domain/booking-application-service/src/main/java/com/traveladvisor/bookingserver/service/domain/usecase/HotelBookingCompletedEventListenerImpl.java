package com.traveladvisor.bookingserver.service.domain.usecase;

import com.traveladvisor.bookingserver.service.domain.dto.message.HotelBookingResponse;
import com.traveladvisor.bookingserver.service.domain.port.input.event.HotelBookingCompletedEventListener;
import com.traveladvisor.bookingserver.service.domain.usecase.saga.BookingHotelSagaAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@RequiredArgsConstructor
@Service
public class HotelBookingCompletedEventListenerImpl implements HotelBookingCompletedEventListener {

    private final BookingHotelSagaAction bookingHotelSagaAction;

    @Override
    public void processHotelBooking(HotelBookingResponse hotelBookingResponse) {
        bookingHotelSagaAction.process(hotelBookingResponse);
    }

    @Override
    public void compensateHotelBooking(HotelBookingResponse hotelBookingResponse) {
        bookingHotelSagaAction.compensate(hotelBookingResponse);
    }

}
