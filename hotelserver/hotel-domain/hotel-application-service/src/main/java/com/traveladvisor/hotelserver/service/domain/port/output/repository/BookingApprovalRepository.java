package com.traveladvisor.hotelserver.service.domain.port.output.repository;

import com.traveladvisor.hotelserver.service.domain.entity.BookingApproval;

public interface BookingApprovalRepository {

    /**
     * 예약 결과를 저장합니다.
     *
     * @param bookingApproval
     * @return
     */
    BookingApproval save(BookingApproval bookingApproval);

}
