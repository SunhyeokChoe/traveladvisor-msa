package com.traveladvisor.hotelserver.service.domain.mapper;

import com.traveladvisor.common.domain.event.hotel.HotelBookingApprovedEventPayload;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.HotelOfferId;
import com.traveladvisor.hotelserver.service.domain.dto.command.CompleteHotelBookingCommand;
import com.traveladvisor.hotelserver.service.domain.entity.BookingApproval;
import com.traveladvisor.hotelserver.service.domain.event.BookingApprovalEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookingApprovalMapper {

    /**
     * CompleteHotelBookingCommand -> BookingApproval
     *
     * @param completeHotelBookingCommand
     * @return BookingApproval
     */
    public BookingApproval toBookingApproval(CompleteHotelBookingCommand completeHotelBookingCommand) {
        return BookingApproval.builder()
                .bookingApprovalId(new BookingApprovalId(UUID.fromString(completeHotelBookingCommand.id())))
                .bookingId(new BookingId(UUID.fromString(completeHotelBookingCommand.bookingId())))
                .hotelOffersId(new HotelOfferId(Long.parseLong(completeHotelBookingCommand.hotelOfferId())))
                .status(completeHotelBookingCommand.bookingStatus())
                .build();
    }

    /**
     * BookingApprovalEvent -> HotelBookingApprovedEventPayload
     *
     * @param bookingApprovalEvent
     * @return HotelBookingApprovedEventPayload
     */
    public HotelBookingApprovedEventPayload toHotelBookingCompletedEventPayload(BookingApprovalEvent bookingApprovalEvent) {
        return HotelBookingApprovedEventPayload.builder()
                .bookingId(bookingApprovalEvent.getBookingApproval().getBookingId().toString())
                .hotelOfferId(bookingApprovalEvent.getBookingApproval().getHotelOffersId().toString())
                .createdAt(bookingApprovalEvent.getCreatedAt())
                .hotelBookingStatus(bookingApprovalEvent.getBookingApproval().getStatus().toString())
                .build();
    }

}
