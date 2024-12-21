package com.traveladvisor.hotelserver.service.application.rest;

import com.traveladvisor.common.application.dto.ErrorResponseDto;
import com.traveladvisor.hotelserver.service.domain.dto.query.QueryHotelOffersResponse;
import com.traveladvisor.hotelserver.service.domain.dto.query.QueryHotelsResponse;
import com.traveladvisor.hotelserver.service.domain.port.input.service.HotelApplicationService;
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
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(
        name = "[Driving Adapter] Hotel Service REST API Controller",
        description = "Hotel CRUD REST API 컨트롤러 입니다."
)
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class HotelController {

    private final HotelApplicationService hotelApplicationService;

    @Operation(
            summary = "Hotels 목록 조회 REST API",
            description = "호텔 메타데이터 목록을 조회하는 페이지네이션 기반 REST API 입니다."
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
    @GetMapping(value = "/hotels")
    public ResponseEntity<Page<QueryHotelsResponse>> queryHotels(
            @ParameterObject @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(hotelApplicationService.queryHotels(pageable));
    }

    @Operation(
            summary = "Hotel Offers 단일 조회 REST API",
            description = "특정 호텔 예약 가능 객실을 ID 기반으로 조회하는 REST API 입니다."
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
    @GetMapping(value = "/hotel-offers/{id}")
    public ResponseEntity<Optional<QueryHotelOffersResponse>> queryHotelOffers(
            @PathVariable("id") Long hotelOfferId) {
        return ResponseEntity.ok(hotelApplicationService.queryHotelOffer(hotelOfferId));
    }

    @Operation(
            summary = "Hotel Offers 목록 조회 REST API",
            description = "호텔 예약 가능 객실 목록을 조회하는 페이지네이션 기반 REST API 입니다."
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
    @GetMapping(value = "/hotel-offers")
    public ResponseEntity<Page<QueryHotelOffersResponse>> queryHotelOffers(
            @RequestParam(required = false) Long hotelOfferId,
            @ParameterObject @PageableDefault(size = 20, sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(hotelApplicationService.queryHotelOffers(pageable));
    }

}
