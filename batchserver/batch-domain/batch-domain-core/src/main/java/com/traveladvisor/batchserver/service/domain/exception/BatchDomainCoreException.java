package com.traveladvisor.batchserver.service.domain.exception;

import com.traveladvisor.common.domain.exception.DomainException;

public class BatchDomainCoreException extends DomainException {

    public BatchDomainCoreException(String message) {
        super(message);
    }

    public BatchDomainCoreException(String message, Throwable cause) {
        super(message, cause);
    }

}
