package com.traveladvisor.bookingserver.service.message.outbound.adapter;

import com.traveladvisor.bookingserver.service.domain.dto.query.QueryMemberResponse;
import com.traveladvisor.bookingserver.service.domain.port.output.client.MemberServiceApiClient;
import com.traveladvisor.bookingserver.service.message.outbound.client.MemberServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MemberServiceApiClientImpl implements MemberServiceApiClient {

    private final MemberServiceFeignClient memberServiceFeignClient;

    @Override
    public ResponseEntity<QueryMemberResponse> queryMember(String correlationId, String email) {
        return memberServiceFeignClient.queryMember(correlationId, email);
    }

}
