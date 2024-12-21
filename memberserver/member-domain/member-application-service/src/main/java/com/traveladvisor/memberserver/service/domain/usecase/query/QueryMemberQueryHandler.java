package com.traveladvisor.memberserver.service.domain.usecase.query;

import com.traveladvisor.memberserver.service.domain.MemberDomainService;
import com.traveladvisor.memberserver.service.domain.dto.query.QueryMemberResponse;
import com.traveladvisor.memberserver.service.domain.entity.Member;
import com.traveladvisor.memberserver.service.domain.exception.MemberApplicationServiceException;
import com.traveladvisor.memberserver.service.domain.mapper.MemberMapper;
import com.traveladvisor.memberserver.service.domain.port.output.client.PaymentServiceApiClient;
import com.traveladvisor.memberserver.service.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class QueryMemberQueryHandler {

    private final MemberDomainService memberDomainService;
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

    private final PaymentServiceApiClient paymentServiceApiClient;

    public QueryMemberResponse queryMember(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new MemberApplicationServiceException("존재하지 않는 회원입니다.");
                });

        return memberMapper.toQueryMemberResponse(member);
    }

}
