package com.traveladvisor.carserver.service.domain.port.output.repository;

import com.traveladvisor.carserver.service.domain.entity.BookingApproval;
import com.traveladvisor.common.domain.vo.BookingApprovalId;

import java.util.Optional;

public interface BookingApprovalRepository {

    /**
     * 차량 예약 결과를 저장합니다.
     *
     * @param bookingApproval
     * @return
     */
    BookingApproval save(BookingApproval bookingApproval);

    Optional<BookingApproval> findById(BookingApprovalId bookingApprovalId);

}
