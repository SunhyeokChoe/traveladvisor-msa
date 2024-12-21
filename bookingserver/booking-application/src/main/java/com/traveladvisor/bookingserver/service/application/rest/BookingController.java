package com.traveladvisor.bookingserver.service.application.rest;

import com.traveladvisor.bookingserver.service.domain.dto.command.CreateBookingCommand;
import com.traveladvisor.bookingserver.service.domain.dto.command.CreateBookingResponse;
import com.traveladvisor.bookingserver.service.domain.dto.query.QueryBookingResponse;
import com.traveladvisor.bookingserver.service.domain.port.input.service.BookingApplicationService;
import com.traveladvisor.common.application.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.traveladvisor.common.domain.constant.common.TraceConstants.CORRELATION_ID;

@Tag(
        name = "[Driving Adapter] Booking Service REST API Controller",
        description = "Booking CRUD REST API 컨트롤러 입니다."
)
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class BookingController {

    private final BookingApplicationService bookingApplicationService;

    @Operation(
            summary = "호텔/항공권/차량 예약 REST API",
            description = "호텔/항공권/차량 예약을 한 번에 진행해주는 REST API 입니다."
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
    @PostMapping("/bookings")
    public ResponseEntity<CreateBookingResponse> createBooking(
            @RequestHeader(CORRELATION_ID) String correlationId,
            @RequestBody @Valid CreateBookingCommand createBookingCommand) {
        CreateBookingResponse createBookingResponse = bookingApplicationService.createBooking(createBookingCommand, correlationId);

        return ResponseEntity.ok(createBookingResponse);
    }

    @Operation(
            summary = "호텔/항공권/차량 예약 조회 REST API",
            description = "호텔/항공권/차량 예약 진행 현황을 조회하는 REST API 입니다."
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
    @GetMapping("/bookings/{traceId}")
    public ResponseEntity<QueryBookingResponse> queryBooking(@PathVariable UUID traceId) {
        QueryBookingResponse queryBookingResponse = bookingApplicationService.queryBooking(traceId);

        return ResponseEntity.ok(queryBookingResponse);
    }

}
