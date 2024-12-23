package com.traveladvisor.bookingserver.service.message.mapper;

import com.traveladvisor.bookingserver.service.domain.dto.message.FlightBookingResponse;
import com.traveladvisor.bookingserver.service.domain.dto.message.HotelBookingResponse;
import com.traveladvisor.common.domain.event.flight.FlightBookingCompletedEventPayload;
import com.traveladvisor.common.domain.event.hotel.HotelBookingCompletedEventPayload;
import com.traveladvisor.common.domain.vo.FlightBookingApprovalStatus;
import com.traveladvisor.common.domain.vo.HotelBookingApprovalStatus;
import org.springframework.stereotype.Component;

@Component
public class BookingMessageMapper {

    public HotelBookingResponse toHotelBookingResponse(HotelBookingCompletedEventPayload hotelBookingCompletedEventPayload,
                                                       debezium.hotel.booking_outbox.Value hotelBookingCompletedEventAvroModel) {

        return new HotelBookingResponse(
                hotelBookingCompletedEventAvroModel.getId(),
                hotelBookingCompletedEventAvroModel.getSagaActionId(),
                hotelBookingCompletedEventPayload.getBookingId(),
                hotelBookingCompletedEventPayload.getHotelOfferId(),
                hotelBookingCompletedEventPayload.getCreatedAt(),
                HotelBookingApprovalStatus.valueOf(hotelBookingCompletedEventPayload.getHotelBookingApprovalStatus()),
                hotelBookingCompletedEventPayload.getFailureMessages()
        );
    }

    public FlightBookingResponse toFlightBookingResponse(FlightBookingCompletedEventPayload flightBookingCompletedEventPayload,
                                                         debezium.flight.booking_outbox.Value flightBookingCompletedEventAvroModel) {

        return new FlightBookingResponse(
                flightBookingCompletedEventAvroModel.getId(),
                flightBookingCompletedEventAvroModel.getSagaActionId(),
                flightBookingCompletedEventPayload.getBookingId(),
                flightBookingCompletedEventPayload.getFlightOfferId(),
                flightBookingCompletedEventPayload.getCreatedAt(),
                FlightBookingApprovalStatus.valueOf(flightBookingCompletedEventPayload.getFlightBookingApprovalStatus()),
                flightBookingCompletedEventPayload.getFailureMessages()
        );
    }

}
