package com.traveladvisor.flightserver.service.domain.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class FlightNotFoundException extends DomainException {

    public FlightNotFoundException(String message) {
        super(message);
    }

    public FlightNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
