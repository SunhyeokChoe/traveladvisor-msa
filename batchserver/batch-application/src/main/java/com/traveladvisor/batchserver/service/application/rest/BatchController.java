package com.traveladvisor.batchserver.service.application.rest;

import com.traveladvisor.batchserver.service.domain.dto.command.CreateHotelsJobCommand;
import com.traveladvisor.batchserver.service.domain.port.input.service.BatchApplicationService;
import com.traveladvisor.common.application.dto.ErrorResponseDto;
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
        name = "[Driving Adapter] Batch Service REST API Controller",
        description = "배치 프로세싱을 위한 CRUD REST API 컨트롤러 입니다."
)
@RequiredArgsConstructor
@Validated
@RestController
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
public class BatchController {

    private final BatchApplicationService batchApplicationService;

    @Operation(
            summary = "Hotels 생성 REST API",
            description = "Amadeus OTA 호텔 데이터 공급업체로부터 호텔 가데이터를 생성하는 REST API 입니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status: OK",
                    content = @Content(
                            schema = @Schema(implementation = CreateHotelsJobCommand.class)
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
            value = "/test-hotels",
            produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<CreateHotelsJobCommand> createHotels(@Valid CreateHotelsJobCommand createHotelsJobCommand) {
        batchApplicationService.createHotels(createHotelsJobCommand);

        return new ResponseEntity<>(createHotelsJobCommand, HttpStatus.OK);
    }

}
