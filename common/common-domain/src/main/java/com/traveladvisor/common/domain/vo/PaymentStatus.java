package com.traveladvisor.common.domain.vo;

public enum PaymentStatus {

    PENDING("결제 대기 중"),
    COMPLETED("결제 완료"),
    CANCELLED("결제 취소 완료"),
    FAILED("결제 실패");

    private final String description;

    PaymentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
