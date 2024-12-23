package com.traveladvisor.carserver.service.domain.mapper;

import com.traveladvisor.carserver.service.domain.dto.query.command.CarBookingCommand;
import com.traveladvisor.carserver.service.domain.entity.BookingApproval;
import com.traveladvisor.carserver.service.domain.event.CarBookingEvent;
import com.traveladvisor.common.domain.event.car.CarBookingCompletedEventPayload;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.CarOfferId;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BookingApprovalMapper {

    /**
     * CarBookingCommand -> BookingApproval
     *
     * @param carBookingCommand CarBookingCommand
     * @return BookingApproval
     */
    public BookingApproval toBookingApproval(CarBookingCommand carBookingCommand) {
        return BookingApproval.builder()
                .bookingApprovalId(new BookingApprovalId(UUID.fromString(carBookingCommand.id())))
                .bookingId(new BookingId(UUID.fromString(carBookingCommand.bookingId())))
                .carOfferId(new CarOfferId(Long.parseLong(carBookingCommand.carOfferId())))
                .status(carBookingCommand.bookingStatus())
                .build();
    }

    /**
     * CarBookingEvent -> CarBookingCompletedEventPayload
     *
     * @param carBookingEvent CarBookingEvent
     * @return CarBookingCompletedEventPayload
     */
    public CarBookingCompletedEventPayload toCarBookingCompletedEventPayload(CarBookingEvent carBookingEvent) {
        return CarBookingCompletedEventPayload.builder()
                .bookingId(carBookingEvent.getBookingApproval().getBookingId().toString())
                .carOfferId(carBookingEvent.getBookingApproval().getCarOfferId().toString())
                .createdAt(carBookingEvent.getCreatedAt())
                .carBookingApprovalStatus(carBookingEvent.getBookingApproval().getStatus().toString())
                .build();
    }

}
