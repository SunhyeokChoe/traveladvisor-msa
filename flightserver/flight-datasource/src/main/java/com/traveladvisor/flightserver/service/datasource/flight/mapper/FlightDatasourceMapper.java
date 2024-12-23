package com.traveladvisor.flightserver.service.datasource.flight.mapper;

import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.FlightOfferId;
import com.traveladvisor.flightserver.service.datasource.entity.BookingApprovalEntity;
import com.traveladvisor.flightserver.service.domain.entity.BookingApproval;
import org.springframework.stereotype.Component;

@Component
public class FlightDatasourceMapper {

    /**
     * BookingApprovalEntity -> BookingApproval
     *
     * @param bookingApprovalEntity BookingApprovalEntity
     * @return BookingApproval
     */
    public BookingApproval toDomain(BookingApprovalEntity bookingApprovalEntity) {
        return BookingApproval.builder()
                .bookingApprovalId(new BookingApprovalId(bookingApprovalEntity.getId()))
                .bookingId(new BookingId(bookingApprovalEntity.getBookingId()))
                .flightOfferId(new FlightOfferId(bookingApprovalEntity.getHotelOfferId()))
                .status(bookingApprovalEntity.getStatus())
                .build();
    }

    /**
     * BookingApproval -> BookingApprovalEntity
     *
     * @param bookingApproval BookingApproval
     * @return BookingApprovalEntity
     */
    public BookingApprovalEntity toEntity(BookingApproval bookingApproval) {
        return BookingApprovalEntity.builder()
                .id(bookingApproval.getId().getValue())
                .bookingId(bookingApproval.getBookingId().getValue())
                .hotelOfferId(bookingApproval.getFlightOfferId().getValue())
                .status(bookingApproval.getStatus())
                .build();
    }

}
