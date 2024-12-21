package com.traveladvisor.flightserver.service.domain.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class FlightApplicationServiceException extends DomainException {

    public FlightApplicationServiceException(String message) {
        super(message);
    }

    public FlightApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
