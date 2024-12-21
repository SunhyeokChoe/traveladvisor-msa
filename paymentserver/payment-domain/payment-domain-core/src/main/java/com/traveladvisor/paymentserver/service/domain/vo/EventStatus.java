package com.traveladvisor.paymentserver.service.domain.vo;

/**
 * 이벤트 진행 상태입니다.
 */
public enum EventStatus {
    WILL_OPEN("오픈 예정"),
    ACTIVE("진행 중"),
    CLOSE("종료");

    private final String description;

    EventStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
