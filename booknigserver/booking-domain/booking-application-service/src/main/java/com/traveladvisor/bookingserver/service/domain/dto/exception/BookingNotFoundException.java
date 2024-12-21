package com.traveladvisor.bookingserver.service.domain.dto.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class BookingNotFoundException extends DomainException {

    public BookingNotFoundException(String message) {
        super(message);
    }

    public BookingNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
