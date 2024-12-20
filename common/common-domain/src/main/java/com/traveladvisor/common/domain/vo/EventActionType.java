package com.traveladvisor.common.domain.vo;

/**
 * 이벤트 액션 타입을 나타내는 Enum 클래스입니다.
 */
public enum EventActionType {
    SIGNUP("회원가입"),
    REFERRAL("추천인 추천"),
    REVIEW("리뷰 작성");

    private final String description;

    EventActionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
