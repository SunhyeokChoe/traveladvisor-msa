package com.traveladvisor.hotelserver.service.domain.mapper;

import com.traveladvisor.common.domain.event.hotel.HotelBookingCompletedEventPayload;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.HotelOfferId;
import com.traveladvisor.hotelserver.service.domain.dto.command.HotelBookingCommand;
import com.traveladvisor.hotelserver.service.domain.entity.BookingApproval;
import com.traveladvisor.hotelserver.service.domain.event.HotelBookingEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookingApprovalMapper {

    /**
     * CompleteHotelBookingCommand -> BookingApproval
     *
     * @param hotelBookingCommand
     * @return BookingApproval
     */
    public BookingApproval toBookingApproval(HotelBookingCommand hotelBookingCommand) {
        return BookingApproval.builder()
                .bookingApprovalId(new BookingApprovalId(UUID.fromString(hotelBookingCommand.id())))
                .bookingId(new BookingId(UUID.fromString(hotelBookingCommand.bookingId())))
                .hotelOffersId(new HotelOfferId(Long.parseLong(hotelBookingCommand.hotelOfferId())))
                .status(hotelBookingCommand.bookingStatus())
                .build();
    }

    /**
     * BookingApprovalEvent -> HotelBookingApprovedEventPayload
     *
     * @param hotelBookingEvent
     * @return HotelBookingApprovedEventPayload
     */
    public HotelBookingCompletedEventPayload toHotelBookingCompletedEventPayload(HotelBookingEvent hotelBookingEvent) {
        return HotelBookingCompletedEventPayload.builder()
                .bookingId(hotelBookingEvent.getBookingApproval().getBookingId().toString())
                .hotelOfferId(hotelBookingEvent.getBookingApproval().getHotelOffersId().toString())
                .createdAt(hotelBookingEvent.getCreatedAt())
                .hotelBookingApprovalStatus(hotelBookingEvent.getBookingApproval().getStatus().toString())
                .build();
    }

}
