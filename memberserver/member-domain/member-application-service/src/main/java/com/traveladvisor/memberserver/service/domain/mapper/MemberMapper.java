package com.traveladvisor.memberserver.service.domain.mapper;

import com.traveladvisor.common.domain.vo.MemberId;
import com.traveladvisor.memberserver.service.domain.dto.command.CreateMemberCommand;
import com.traveladvisor.memberserver.service.domain.dto.command.CreateMemberResponse;
import com.traveladvisor.memberserver.service.domain.dto.query.QueryMemberResponse;
import com.traveladvisor.memberserver.service.domain.entity.Member;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MemberMapper {

    public Member toMember(CreateMemberCommand createMemberCommand) {
        return Member.builder()
                .memberId(new MemberId(UUID.randomUUID()))
                .email(createMemberCommand.email())
                .nickname(createMemberCommand.nickname())
                .firstName(createMemberCommand.firstName())
                .lastName(createMemberCommand.lastName())
                .contactNumber(createMemberCommand.contactNumber())
                .gender(createMemberCommand.gender())
                .build();
    }

    public CreateMemberResponse toCreateMemberResponse(Member member, String message) {
        return new CreateMemberResponse(member.getEmail(), message);
    }

    public QueryMemberResponse toQueryMemberResponse(Member member) {
        return new QueryMemberResponse(member.getEmail(), member.getNickname());
    }

}
