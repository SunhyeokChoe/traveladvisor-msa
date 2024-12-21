package com.traveladvisor.bookingserver.service.domain.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class BookingDomainCoreException extends DomainException {

    public BookingDomainCoreException(String message) {
        super(message);
    }

    public BookingDomainCoreException(String message, Throwable cause) {
        super(message, cause);
    }

}
