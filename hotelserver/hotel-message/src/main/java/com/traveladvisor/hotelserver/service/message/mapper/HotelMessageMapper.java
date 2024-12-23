package com.traveladvisor.hotelserver.service.message.mapper;

import com.traveladvisor.common.domain.event.booking.BookingCreatedEventPayload;
import com.traveladvisor.common.domain.vo.HotelBookingStatus;
import com.traveladvisor.hotelserver.service.domain.dto.command.HotelBookingCommand;
import debezium.booking.hotel_outbox.Value;
import org.springframework.stereotype.Component;

@Component
public class HotelMessageMapper {

    public HotelBookingCommand toHotelBookingCommand(BookingCreatedEventPayload bookingCreatedEventPayload,
                                                     Value hotelBookingRequestAvroModel) {
        return new HotelBookingCommand(
                hotelBookingRequestAvroModel.getId(),
                hotelBookingRequestAvroModel.getSagaActionId(),
                bookingCreatedEventPayload.getBookingId(),
                bookingCreatedEventPayload.getHotelOfferId(),
                bookingCreatedEventPayload.getMemberEmail(),
                bookingCreatedEventPayload.getTotalPrice(),
                HotelBookingStatus.valueOf(bookingCreatedEventPayload.getHotelBookingStatus()),
                bookingCreatedEventPayload.getCreatedAt()
        );
    }

}
