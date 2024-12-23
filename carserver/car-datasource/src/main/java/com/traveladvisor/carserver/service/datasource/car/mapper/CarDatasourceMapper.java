package com.traveladvisor.carserver.service.datasource.car.mapper;

import com.traveladvisor.carserver.service.datasource.car.entity.BookingApprovalEntity;
import com.traveladvisor.carserver.service.domain.entity.BookingApproval;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.CarOfferId;
import org.springframework.stereotype.Component;

@Component
public class CarDatasourceMapper {

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
                .carOfferId(new CarOfferId(bookingApprovalEntity.getCarOfferId()))
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
                .carOfferId(bookingApproval.getCarOfferId().getValue())
                .status(bookingApproval.getStatus())
                .build();
    }

}
