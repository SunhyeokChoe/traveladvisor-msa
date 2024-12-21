package com.traveladvisor.carserver.service.domain.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class CarApplicationServiceException extends DomainException {

    public CarApplicationServiceException(String message) {
        super(message);
    }

    public CarApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
