package com.traveladvisor.batchserver.service.domain.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class BatchApplicationServiceException extends DomainException {

    public BatchApplicationServiceException(String message) {
        super(message);
    }

    public BatchApplicationServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
