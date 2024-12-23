package com.traveladvisor.hotelserver.service.domain;

import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.hotelserver.service.domain.entity.HotelOffer;
import com.traveladvisor.hotelserver.service.domain.event.HotelBookingEvent;

import java.util.List;

public interface HotelDomainService {

    /**
     * 예약 가능 호텔 객실 데이터가 유효한지 검증하고 예약 성공 상태로 초기화 합니다.
     *
     * @param hotelOffer
     * @param failureMessages
     * @return
     */
    HotelBookingEvent initializeBookingApproval(
            BookingId bookingId, HotelOffer hotelOffer, List<String> failureMessages);

    /**
     * 예약 가능 호텔 객실 데이터가 유효한지 검증하고 예약 취소 상태로 초기화 합니다.
     *
     * @param bookingId
     * @param hotelOffer
     * @param failureMessages
     * @return
     */
    HotelBookingEvent cancelBookingApproval(
            BookingId bookingId, HotelOffer hotelOffer, List<String> failureMessages);

}
