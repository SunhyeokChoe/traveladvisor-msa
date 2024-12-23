package com.traveladvisor.common.domain.vo;

public enum HotelBookingStatus {

    /**
     * 호텔 예약 진행 중
     */
    PENDING,

    /**
     * 호텔 예약 취소 완료
     */
    CANCELLED,

    // TODO: 삭제
    /**
     * 호텔 예약 완료
     */
    HOTEL_BOOKED,

    // TODO: 삭제
    /**
     * 호텔 예약 실패
     */
    HOTEL_REJECTED

}
