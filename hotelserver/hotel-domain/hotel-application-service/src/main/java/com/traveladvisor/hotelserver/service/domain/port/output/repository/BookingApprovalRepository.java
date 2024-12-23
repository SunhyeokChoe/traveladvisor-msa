package com.traveladvisor.hotelserver.service.domain.port.output.repository;

import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.hotelserver.service.domain.entity.BookingApproval;

import java.util.Optional;

public interface BookingApprovalRepository {

    /**
     * 호텔 객실 예약 결과를 저장합니다.
     *
     * @param bookingApproval
     * @return
     */
    BookingApproval save(BookingApproval bookingApproval);

    Optional<BookingApproval> findById(BookingApprovalId bookingApprovalId);

}
