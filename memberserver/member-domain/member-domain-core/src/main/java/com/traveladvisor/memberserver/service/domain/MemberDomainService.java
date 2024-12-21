package com.traveladvisor.memberserver.service.domain;

import com.traveladvisor.memberserver.service.domain.entity.Member;
import com.traveladvisor.memberserver.service.domain.event.MemberCreatedEvent;

public interface MemberDomainService {

    MemberCreatedEvent initializeMember(Member member);

}
