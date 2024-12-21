package com.traveladvisor.paymentserver.service.domain.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class PaymentApplicationServiceException extends DomainException {

    public PaymentApplicationServiceException(String message) {
        super(message);
    }

    public PaymentApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
