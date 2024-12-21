package com.traveladvisor.bookingserver.service.domain.port.output.client;

import com.traveladvisor.bookingserver.service.domain.dto.query.QueryMemberResponse;
import org.springframework.http.ResponseEntity;

public interface MemberServiceApiClient {

    /**
     * 회원 정보를 조회합니다.
     *
     * @param email 회원 이메일
     * @return
     */
    ResponseEntity<QueryMemberResponse> queryMember(String correlationId, String email);

}
