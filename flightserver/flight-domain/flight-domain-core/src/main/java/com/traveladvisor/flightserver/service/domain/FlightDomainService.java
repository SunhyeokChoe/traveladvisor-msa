package com.traveladvisor.flightserver.service.domain;

import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.flightserver.service.domain.entity.FlightOffer;
import com.traveladvisor.flightserver.service.domain.event.FlightBookingEvent;

import java.util.List;

public interface FlightDomainService {

    /**
     * 예약 가능 항공권 데이터가 유효한지 검증하고 초기화 합니다.
     *
     * @param flightOffer
     * @param failureMessages
     * @return
     */
    FlightBookingEvent initializeBookingApproval(
            BookingId bookingId, FlightOffer flightOffer, List<String> failureMessages);

    /**
     * 예약 가능 항공권 데이터가 유효한지 검증하고 예약 취소 상태로 초기화 합니다.
     *
     * @param bookingId
     * @param flightOffer
     * @param failureMessages
     * @return
     */
    FlightBookingEvent cancelBookingApproval(
            BookingId bookingId, FlightOffer flightOffer, List<String> failureMessages);

}
