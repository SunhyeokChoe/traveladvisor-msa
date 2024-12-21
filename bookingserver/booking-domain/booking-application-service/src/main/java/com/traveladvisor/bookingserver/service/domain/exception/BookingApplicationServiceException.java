package com.traveladvisor.bookingserver.service.domain.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class BookingApplicationServiceException extends DomainException {

    public BookingApplicationServiceException(String message) {
        super(message);
    }

    public BookingApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
