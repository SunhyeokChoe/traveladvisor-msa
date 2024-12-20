package com.traveladvisor.memberserver.service.domain.event;

import com.traveladvisor.common.domain.event.DomainEvent;
import com.traveladvisor.member.service.domain.entity.Member;

import java.time.ZonedDateTime;

public class MemberCreatedEvent implements DomainEvent<Member> {

    private final Member member;

    private final ZonedDateTime createdAt;

    public MemberCreatedEvent(Member member, ZonedDateTime createdAt) {
        this.member = member;
        this.createdAt = createdAt;
    }

    public Member getMember() {
        return member;
    }

}
