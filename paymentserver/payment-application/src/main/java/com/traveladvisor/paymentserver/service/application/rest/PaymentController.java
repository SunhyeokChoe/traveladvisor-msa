package com.traveladvisor.paymentserver.service.application.rest;

import com.traveladvisor.common.application.dto.ErrorResponseDto;
import com.traveladvisor.paymentserver.service.domain.dto.command.CreatePointEntryCommand;
import com.traveladvisor.paymentserver.service.domain.dto.command.CreateRewardCommand;
import com.traveladvisor.paymentserver.service.domain.port.input.service.PaymentApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(
        name = "[Driving Adapter] Payment Service REST API Controller",
        description = "Payment CRUD REST API 컨트롤러 입니다."
)
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class PaymentController {

    private final PaymentApplicationService paymentApplicationService;

    @Operation(
            summary = "포인트 계좌 생성 REST API",
            description = "회원의 포인트 계좌를 생성하는 REST API 입니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status: OK",
                    content = @Content(
                            schema = @Schema(implementation = CreatePointEntryCommand.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status: CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status: Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping(
            value = "/point-entries",
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<CreatePointEntryCommand> createPointEntry(@Valid CreatePointEntryCommand createPointEntryCommand) {
        paymentApplicationService.createPointEntry(createPointEntryCommand);

        return new ResponseEntity<>(createPointEntryCommand, HttpStatus.OK);
    }

    @Operation(
            summary = "리워드 지급 REST API",
            description = "진행 중인 이벤트가 존재하는지 확인하고 리워드를 제공하는 REST API 입니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status: OK"
            ),
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status: CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status: Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @PostMapping(
            value = "/rewards",
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity createPointEntry(@Valid CreateRewardCommand createRewardCommand) {
        paymentApplicationService.createReward(createRewardCommand);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
