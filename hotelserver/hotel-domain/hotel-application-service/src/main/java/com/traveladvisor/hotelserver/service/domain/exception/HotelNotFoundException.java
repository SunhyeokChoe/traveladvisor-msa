package com.traveladvisor.hotelserver.service.domain.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class HotelNotFoundException extends DomainException {

    public HotelNotFoundException(String message) {
        super(message);
    }

    public HotelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
