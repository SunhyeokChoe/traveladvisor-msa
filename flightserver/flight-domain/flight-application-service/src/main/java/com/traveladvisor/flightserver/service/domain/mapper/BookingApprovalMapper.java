package com.traveladvisor.flightserver.service.domain.mapper;

import com.traveladvisor.common.domain.event.flight.FlightBookingCompletedEventPayload;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.FlightOfferId;
import com.traveladvisor.flightserver.service.domain.dto.command.FlightBookingCommand;
import com.traveladvisor.flightserver.service.domain.entity.BookingApproval;
import com.traveladvisor.flightserver.service.domain.event.FlightBookingEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookingApprovalMapper {

    /**
     * FlightBookingCommand -> BookingApproval
     *
     * @param flightBookingCommand
     * @return BookingApproval
     */
    public BookingApproval toBookingApproval(FlightBookingCommand flightBookingCommand) {
        return BookingApproval.builder()
                .bookingApprovalId(new BookingApprovalId(UUID.fromString(flightBookingCommand.id())))
                .bookingId(new BookingId(UUID.fromString(flightBookingCommand.bookingId())))
                .flightOfferId(new FlightOfferId(Long.parseLong(flightBookingCommand.flightOfferId())))
                .status(flightBookingCommand.bookingStatus())
                .build();
    }

    /**
     * FlightBookingEvent -> FlightBookingCompletedEventPayload
     *
     * @param flightBookingEvent
     * @return FlightBookingCompletedEventPayload
     */
    public FlightBookingCompletedEventPayload toFlightBookingCompletedEventPayload(FlightBookingEvent flightBookingEvent) {
        return FlightBookingCompletedEventPayload.builder()
                .bookingId(flightBookingEvent.getBookingApproval().getBookingId().toString())
                .flightOfferId(flightBookingEvent.getBookingApproval().getFlightOfferId().toString())
                .createdAt(flightBookingEvent.getCreatedAt())
                .flightBookingApprovalStatus(flightBookingEvent.getBookingApproval().getStatus().toString())
                .build();
    }

}
