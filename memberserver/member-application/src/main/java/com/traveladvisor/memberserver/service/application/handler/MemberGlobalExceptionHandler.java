package com.traveladvisor.memberserver.service.application.handler;

import com.traveladvisor.common.application.dto.ErrorResponseDto;
import com.traveladvisor.common.application.exception.GlobalExceptionHandler;
import com.traveladvisor.memberserver.service.domain.exception.MemberApplicationServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class MemberGlobalExceptionHandler extends GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {MemberApplicationServiceException.class})
    public ResponseEntity<ErrorResponseDto> handleException(MemberApplicationServiceException ex, WebRequest webRequest) {
        log.error("[Member Domain] 예외가 발생했습니다. 내용은 다음과 같습니다.", ex);

        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                "예상치 못한 서버 에러가 발생했습니다. 고객 센터에 문의 바랍니다.",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
