package com.traveladvisor.carserver.service.domain.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class CarNotFoundException extends DomainException {

    public CarNotFoundException(String message) {
        super(message);
    }

    public CarNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
