package com.traveladvisor.hotelserver.service.domain.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class HotelDomainCoreException extends DomainException {

    public HotelDomainCoreException(String message) {
        super(message);
    }

    public HotelDomainCoreException(String message, Throwable cause) {
        super(message, cause);
    }

}
