package com.traveladvisor.memberserver.service.domain;

import com.traveladvisor.memberserver.service.domain.entity.Member;
import com.traveladvisor.memberserver.service.domain.event.MemberCreatedEvent;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.traveladvisor.common.domain.constant.common.DomainConstants.UTC;

@Slf4j
public class MemberDomainServiceImpl implements MemberDomainService {

    @Override
    public MemberCreatedEvent initializeMember(Member member) {
        // TODO: 회원 정보 초기화
        //       .....
        //       .....

        log.info("Member 도메인 엔터티가 초기화 됐습니다. {}", member.getId().getValue());

        return new MemberCreatedEvent(member, ZonedDateTime.now(ZoneId.of(UTC)));
    }

}
