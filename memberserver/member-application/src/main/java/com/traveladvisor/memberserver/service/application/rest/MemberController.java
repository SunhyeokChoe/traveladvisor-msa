package com.traveladvisor.memberserver.service.application.rest;

import com.traveladvisor.common.application.dto.ErrorResponseDto;
import com.traveladvisor.memberserver.service.domain.dto.command.CreateMemberCommand;
import com.traveladvisor.memberserver.service.domain.dto.command.CreateMemberResponse;
import com.traveladvisor.memberserver.service.domain.dto.query.QueryMemberResponse;
import com.traveladvisor.memberserver.service.domain.port.input.service.MemberApplicationService;
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

import static com.traveladvisor.common.domain.constant.common.TraceConstants.CORRELATION_ID;

@Tag(
        name = "[Driving Adapter] Member Service REST API Controller",
        description = "Member CRUD REST API 컨트롤러 입니다."
)
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/api", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class MemberController {

    private final MemberApplicationService memberApplicationService;

    @Operation(
            summary = "회원 단일 조회 REST API",
            description = "email에 해당하는 회원을 조회하는 REST API 입니다."
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
    @GetMapping(value = "/members/{email}")
    public ResponseEntity<QueryMemberResponse> queryMember(@PathVariable("email") String email) {
        return ResponseEntity.ok(memberApplicationService.queryMember(email));
    }

    @Operation(
            summary = "회원가입 REST API",
            description = "회원가입 REST API 입니다."
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
    @PostMapping("/sign-up")
    public ResponseEntity<CreateMemberResponse> createMember(@RequestHeader(CORRELATION_ID) String correlationId,
                                                             @RequestBody @Valid CreateMemberCommand createMemberCommand) {
        return ResponseEntity.ok(memberApplicationService.createMember(createMemberCommand, correlationId));
    }

}
