package com.traveladvisor.memberserver.service.domain.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class MemberApplicationServiceException extends DomainException {

    public MemberApplicationServiceException(String message) {
        super(message);
    }

    public MemberApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
