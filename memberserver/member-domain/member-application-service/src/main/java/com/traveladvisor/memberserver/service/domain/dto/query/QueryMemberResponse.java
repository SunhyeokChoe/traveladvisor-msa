package com.traveladvisor.memberserver.service.domain.dto.query;

/**
 * 회원 조회 요청 응답
 */
public record QueryMemberResponse(
        String email,
        String message

) {

}
