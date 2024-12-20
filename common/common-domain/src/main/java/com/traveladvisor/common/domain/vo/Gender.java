package com.traveladvisor.common.domain.vo;

/**
 * 성별을 나타내는 Enum 클래스입니다.
 */
public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    OTHER("기타"),
    UNDISCLOSED("공개하지 않음");

    private final String description;

    Gender(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
