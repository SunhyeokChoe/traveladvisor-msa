package com.traveladvisor.flightserver.service.message.mapper;

import com.traveladvisor.common.domain.event.booking.HotelBookedEventPayload;
import com.traveladvisor.common.domain.vo.FlightBookingApprovalStatus;
import com.traveladvisor.flightserver.service.domain.dto.command.FlightBookingCommand;
import debezium.booking.flight_outbox.Value;
import org.springframework.stereotype.Component;

@Component
public class FlightMessageMapper {

    public FlightBookingCommand toFlightBookingCommand(HotelBookedEventPayload hotelBookedEventPayload,
                                                       Value flightBookingRequestAvroModel) {
        return new FlightBookingCommand(
                flightBookingRequestAvroModel.getId(),
                flightBookingRequestAvroModel.getSagaActionId(),
                hotelBookedEventPayload.getBookingId(),
                hotelBookedEventPayload.getFlightOfferId(),
                hotelBookedEventPayload.getMemberEmail(),
                hotelBookedEventPayload.getTotalPrice(),
                FlightBookingApprovalStatus.valueOf(hotelBookedEventPayload.getFlightBookingStatus()),
                hotelBookedEventPayload.getCreatedAt()
        );
    }

}
