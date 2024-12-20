package com.traveladvisor.common.domain.vo;

/**
 * 예약 흐름은 다음과 같습니다.
 *
 * 1. 예약 생성 시 PENDING 상태로 기록합니다.
 * 2. 호텔 서비스로 이벤트를 전달합니다.
 *    2-1. 호텔 예약 성공 -> 예약 상태를 HOTEL_BOOKED로 기록합니다.
 *    2-2. 호텔 예약 실패 -> 예약 상태를 HOTEL_CANCELLED로 기록합니다.
 * 3. 항공권 서비스로 이벤트를 전달합니다.
 *    3-1. 항공권 예약 성공 -> 예약 상태를 FLIGHT_BOOKED로 기록합니다.
 *    3-2. 항공권 예약 실패 -> 예약 상태를 FLIGHT_CANCELLED로 기록합니다.
 *         {@link com.traveladvisor.common.domain.saga.SagaActionStatus}를 COMPENSATING으로 바꾸고 호텔 서비스에서 트랜잭션 보상이 진행되도록 합니다.
 * 4. 차량 서비스로 이벤트를 전달합니다.
 *    4-1. 차량 예약 성공 -> 예약 상태를 CAR_BOOKED로 기록합니다.
 *    4-2. 차량 예약 실패 -> 예약 상태를 CAR_CANCELLED로 기록합니다.
 *         {@link com.traveladvisor.common.domain.saga.SagaActionStatus}를 COMPENSATING으로 바꾸고 항공권 서비스에서 트랜잭션 보상이 진행되도록 합니다.
 *
 * ※ 트랜잭션 보상 절차는 Saga Action 수행 방향의 역방향으로 순차적으로 진행합니다.
 */
public enum BookingStatus {

    /**
     * 예약 시작
     */
    PENDING,

    /**
     * 호텔 예약 완료
     */
    HOTEL_BOOKED,

    /**
     * 항공권 예약 완료
     */
    FLIGHT_BOOKED,

    /**
     * 차량 예약 완료
     */
    CAR_BOOKED,

    /**
     * 호텔 예약 취소
     */
    HOTEL_CANCELLED,

    /**
     * 항공권 예약 취소
     */
    FLIGHT_CANCELLED,

    /**
     * 차량 예약 취소
     */
    CAR_CANCELLED,

    /**
     * 예약 성공
     */
    APPROVED,

    /**
     * 예약 취소 중
     */
    CANCELLING,

    /**
     * 예약 취소 완료
     */
    CANCELLED

}
