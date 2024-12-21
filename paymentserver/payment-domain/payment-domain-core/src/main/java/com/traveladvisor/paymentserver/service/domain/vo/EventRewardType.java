package com.traveladvisor.paymentserver.service.domain.vo;

/**
 * 이벤트 리워드 타입입니다.
 */
public enum EventRewardType {
    POINT("포인트 지급"),
    GIFT("선물 지급"),
    VOUCHER("교환권 지급"),
    DISCOUNT("할인권 지급");

    private final String description;

    EventRewardType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
