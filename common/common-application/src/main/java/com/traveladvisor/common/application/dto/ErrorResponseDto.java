package com.traveladvisor.common.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Schema(
        name = "ErrorResponse",
        description = "에러 응답 DTO 스키마 입니다."
)
public class ErrorResponseDto {

    @Schema(
            description = "요청된 REST API 경로입니다."
    )
    private String apiPath;

    @Schema(
            description = "에러 코드입니다."
    )
    private HttpStatus errorCode;

    @Schema(
            description = "에러 메시지입니다."
    )
    private String errorMessage;

    @Schema(
            description = "에러가 발생한 시간입니다."
    )
    private LocalDateTime errorTime;

}
