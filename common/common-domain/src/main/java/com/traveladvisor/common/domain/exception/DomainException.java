package com.traveladvisor.common.domain.exception;

/**
 * 도메인에서 런타임 예외를 발생시킬 때 사용하는 예외 공통 클래스 입니다.
 * 각 도메인 모듈에서 이를 확장해 사용합니다.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

}
