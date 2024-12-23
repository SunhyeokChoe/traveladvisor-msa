package com.traveladvisor.flightserver.service.domain.port.output.repository;

import com.traveladvisor.flightserver.service.domain.entity.BookingApproval;

public interface BookingApprovalRepository {

    /**
     * 항공권 예약 결과를 저장합니다.
     *
     * @param bookingApproval
     * @return
     */
    BookingApproval save(BookingApproval bookingApproval);

}
