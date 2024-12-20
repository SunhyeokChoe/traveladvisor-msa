package com.traveladvisor.common.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s 를(을) 찾을 수 없습니다. 요청 데이터 %s : '%s'", resourceName, fieldName, fieldValue));
    }

}
