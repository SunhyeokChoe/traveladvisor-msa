package com.traveladvisor.bookingserver.service.message.outbound.client;

import com.traveladvisor.bookingserver.service.domain.dto.query.QueryMemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import static com.traveladvisor.common.domain.constant.common.TraceConstants.CORRELATION_ID;

@FeignClient(name="member", url = "http://member:9200"/*, fallback = MemberServiceFeignFallback.class*/)
public interface MemberServiceFeignClient {

    /**
     * 회원을 조회합니다.
     *
     * @param correlationId 요청 추적을 위한 상관관계 ID
     * @param email         회원 이메일
     * @return
     */
    @GetMapping(value = "/api/members/{email}",consumes = "application/json")
    ResponseEntity<QueryMemberResponse> queryMember(@RequestHeader(CORRELATION_ID) String correlationId,
                                                    @PathVariable("email") String email);

}
