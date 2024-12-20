package com.traveladvisor.common.application.exception;

import com.traveladvisor.common.application.dto.ErrorResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> validationErrors = new HashMap<>();
        List<ObjectError> validationErrorList = ex.getBindingResult().getAllErrors();

        validationErrorList.forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String validationMsg = error.getDefaultMessage();
            validationErrors.put(fieldName, validationMsg);
        });

        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception ex,
                                                                  WebRequest webRequest) {
        // 서버 내부의 예기치 않은 오류는 클라이언트에게 중요하지 않고, 보안상의 이유로도 내부 오류 메시지를
        // 외부로 공유하는 것은 옳지 않기 때문에 message 필드에 "예상치 못한 서버 에러가 발생했습니다. 고객 센터에 문의 바랍니다." 를 넣어 반환하고,
        // 로그를 남겨 로그로만 추적할 수 있도록 합니다.
        log.error(ex.getMessage(), ex);

        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.INTERNAL_SERVER_ERROR,
                "예상치 못한 서버 에러가 발생했습니다. 고객 센터에 문의 바랍니다.",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex,
                                                                            WebRequest webRequest) {
        ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                webRequest.getDescription(false),
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(value = {ValidationException.class}) // spring-boot-starter-validation을 활용한 유효성 검사 시 예외가 발생한 경우 이 메서드를 트리거하여 핸들링합니다.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDto> handleException(ValidationException validationException,
                                                            WebRequest webRequest) {
        // 서버 내부 오류는 서버에 로그를 남겨 추적할 수 있도록 합니다.
        log.error(validationException.getMessage(), validationException);

        ErrorResponseDto errorResponseDTO;
        if (validationException instanceof ConstraintViolationException) {
            String violations = extractConstraintViolations((ConstraintViolationException) validationException);

            log.error(violations, validationException);

            errorResponseDTO = new ErrorResponseDto(
                    webRequest.getDescription(false),
                    HttpStatus.BAD_REQUEST,
                    validationException.getMessage(),
                    LocalDateTime.now()
            );
        } else {
            String exceptionMessage = validationException.getMessage();

            log.error(exceptionMessage, validationException);

             errorResponseDTO = new ErrorResponseDto(
                    webRequest.getDescription(false),
                    HttpStatus.BAD_REQUEST,
                    validationException.getMessage(),
                    LocalDateTime.now()
            );
        }

        return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    /**
     * ConstraintViolationException으로부터 유효성 검사 실패 사유 메시지를 추출합니다.
     *
     * @param violationException
     * @return
     */
    private String extractConstraintViolations(ConstraintViolationException violationException) {
        return violationException.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("--"));
    }

}
