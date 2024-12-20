package com.traveladvisor.memberserver.service.domain.dto.command;

/**
 * 회원가입 요청 응답
 */
public record CreateMemberResponse(
        String email,
        String message

) {

}
