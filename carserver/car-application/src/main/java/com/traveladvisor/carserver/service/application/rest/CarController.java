package com.traveladvisor.carserver.service.application.rest;

import com.traveladvisor.carserver.service.domain.dto.query.QueryCarOffersResponse;
import com.traveladvisor.carserver.service.domain.port.input.service.CarApplicationService;
import com.traveladvisor.common.application.dto.ErrorResponseDto;
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
        name = "[Driving Adapter] Car Service REST API Controller",
        description = "Car CRUD REST API 컨트롤러 입니다."
)
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class CarController {

    private final CarApplicationService carApplicationService;

    @Operation(
            summary = "Car Offers 단일 조회 REST API",
            description = "예약 가능 차량 ID를 기반으로 조회하는 REST API 입니다."
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
    @GetMapping(value = "/car-offers/{id}")
    public ResponseEntity<Optional<QueryCarOffersResponse>> queryCarOffers(
            @PathVariable("id") Long carOfferId) {
        return ResponseEntity.ok(carApplicationService.queryCarOffer(carOfferId));
    }

    @Operation(
            summary = "Car Offers 목록 조회 REST API",
            description = "예약 가능 렌트 차량 목록을 조회하는 페이지네이션 기반 REST API 입니다."
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
    @GetMapping(value = "/car-offers")
    public ResponseEntity<Page<QueryCarOffersResponse>> queryCarOffers(
            @ParameterObject @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(carApplicationService.queryCarOffers(pageable));
    }

}
