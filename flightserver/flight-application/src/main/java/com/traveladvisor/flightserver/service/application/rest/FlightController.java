package com.traveladvisor.flightserver.service.application.rest;

import com.traveladvisor.common.application.dto.ErrorResponseDto;
import com.traveladvisor.flightserver.service.domain.dto.query.QueryFlightOffersResponse;
import com.traveladvisor.flightserver.service.domain.port.input.service.FlightApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Tag(
        name = "[Driving Adapter] Flight Service REST API Controller",
        description = "Flight CRUD REST API 컨트롤러 입니다."
)
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class FlightController {

    private final FlightApplicationService flightApplicationService;

    @Operation(
            summary = "Flight Offers 단일 조회 REST API",
            description = "특정 예약 가능 항공권을 ID 기반으로 조회하는 REST API 입니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status: OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status: Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping(value = "/flight-offers/{id}")
    public ResponseEntity<Optional<QueryFlightOffersResponse>> queryFlightOffers(
            @PathVariable("id") Long flightOfferId) {
        return ResponseEntity.ok(flightApplicationService.queryFlightOffer(flightOfferId));
    }

    @Operation(
            summary = "Flight Offers 목록 조회 REST API",
            description = "예약 가능 항공권 목록을 조회하는 페이지네이션 기반 REST API 입니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status: OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status: Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @GetMapping(value = "/flight-offers")
    public ResponseEntity<Page<QueryFlightOffersResponse>> queryFlightOffers(
            @ParameterObject @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(flightApplicationService.queryFlightOffers(pageable));
    }

}
