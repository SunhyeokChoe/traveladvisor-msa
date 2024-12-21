package com.traveladvisor.hotelserver.service.domain.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class HotelApplicationServiceException extends DomainException {

    public HotelApplicationServiceException(String message) {
        super(message);
    }

    public HotelApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
