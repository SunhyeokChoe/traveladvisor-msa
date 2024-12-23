package com.traveladvisor.bookingserver.service.message.mapper;

import com.traveladvisor.bookingserver.service.domain.dto.message.HotelBookingResponse;
import com.traveladvisor.common.domain.event.hotel.HotelBookingCompletedEventPayload;
import com.traveladvisor.common.domain.vo.HotelBookingApprovalStatus;
import debezium.hotel.booking_outbox.Value;
import org.springframework.stereotype.Component;

@Component
public class BookingMessageMapper {

    public HotelBookingResponse toHotelBookingResponse(HotelBookingCompletedEventPayload hotelBookingCompletedEventPayload,
                                                       Value hotelBookingCompletedEventAvroModel) {

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

}
