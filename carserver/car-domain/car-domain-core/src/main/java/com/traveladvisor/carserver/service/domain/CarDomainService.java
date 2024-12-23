package com.traveladvisor.carserver.service.domain;

import com.traveladvisor.carserver.service.domain.entity.CarOffer;
import com.traveladvisor.carserver.service.domain.event.CarBookingEvent;
import com.traveladvisor.common.domain.vo.BookingId;

import java.util.List;

public interface CarDomainService {

    /**
     * 예약 가능 차량 데이터가 유효한지 검증하고 초기화 합니다.
     *
     * @param carOffer
     * @param failureMessages
     * @return
     */
    CarBookingEvent initializeBookingApproval(
            BookingId bookingId, CarOffer carOffer, List<String> failureMessages);

}
