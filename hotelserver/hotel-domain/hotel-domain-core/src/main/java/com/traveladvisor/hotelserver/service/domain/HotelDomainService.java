package com.traveladvisor.hotelserver.service.domain;

import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.hotelserver.service.domain.entity.HotelOffer;
import com.traveladvisor.hotelserver.service.domain.event.BookingApprovalEvent;

import java.util.List;

public interface HotelDomainService {

    /**
     * 예약 가능 호텔 객실 데이터가 유효한지 검증하고 초기화 합니다.
     *
     * @param hotelOffer
     * @param failureMessages
     * @return
     */
    BookingApprovalEvent initializeBookingApproval(
            BookingId bookingId, HotelOffer hotelOffer, List<String> failureMessages);

}
