package com.traveladvisor.hotelserver.service.application.handler;

import com.traveladvisor.common.application.dto.ErrorResponseDto;
import com.traveladvisor.common.application.exception.GlobalExceptionHandler;
import com.traveladvisor.hotelserver.service.domain.exception.HotelApplicationServiceException;
import com.traveladvisor.hotelserver.service.domain.exception.HotelNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
public class HotelGlobalExceptionHandler extends GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {HotelApplicationServiceException.class})
    public ResponseEntity<ErrorResponseDto> handleException(HotelApplicationServiceException ex, WebRequest webRequest) {
        log.error("[Hotel Domain] 예외가 발생했습니다. 내용은 다음과 같습니다.", ex);

        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                "예상치 못한 서버 에러가 발생했습니다. 고객 센터에 문의 바랍니다.",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(value = {HotelNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404를 반환합니다.
    public ResponseEntity<ErrorResponseDto> handleException(HotelNotFoundException ex, WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
