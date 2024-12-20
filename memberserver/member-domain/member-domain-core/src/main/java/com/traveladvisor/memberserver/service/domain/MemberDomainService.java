package com.traveladvisor.memberserver.service.domain;

import com.traveladvisor.member.service.domain.entity.Member;
import com.traveladvisor.member.service.domain.event.MemberCreatedEvent;

public interface MemberDomainService {

    MemberCreatedEvent initializeMember(Member member);

}
