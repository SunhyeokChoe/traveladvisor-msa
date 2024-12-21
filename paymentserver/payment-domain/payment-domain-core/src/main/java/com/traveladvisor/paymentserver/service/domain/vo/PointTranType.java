package com.traveladvisor.paymentserver.service.domain.vo;

/**
 * 포인트 계좌 트랜잭션 타입입니다.
 */
public enum PointTranType {

    EARN("포인트 적립"),
    REDEEM("포인트 차감");

    private final String description;

    PointTranType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
