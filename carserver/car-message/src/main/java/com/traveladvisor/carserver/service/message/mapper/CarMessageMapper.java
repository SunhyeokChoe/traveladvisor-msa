package com.traveladvisor.carserver.service.message.mapper;

import com.traveladvisor.carserver.service.domain.dto.query.command.CarBookingCommand;
import com.traveladvisor.common.domain.event.booking.FlightBookedEventPayload;
import com.traveladvisor.common.domain.vo.CarBookingApprovalStatus;
import debezium.booking.car_outbox.Value;
import org.springframework.stereotype.Component;

@Component
public class CarMessageMapper {

    public CarBookingCommand toCarBookingCommand(FlightBookedEventPayload flightBookedEventPayload,
                                                 Value carBookingRequestAvroModel) {
        return new CarBookingCommand(
                carBookingRequestAvroModel.getId(),
                carBookingRequestAvroModel.getSagaActionId(),
                flightBookedEventPayload.getBookingId(),
                flightBookedEventPayload.getCarOfferId(),
                flightBookedEventPayload.getMemberEmail(),
                flightBookedEventPayload.getTotalPrice(),
                CarBookingApprovalStatus.valueOf(flightBookedEventPayload.getCarBookingStatus()),
                flightBookedEventPayload.getCreatedAt()
        );
    }

}
