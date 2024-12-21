package com.traveladvisor.memberserver.service.datasource.member.mapper;

import com.traveladvisor.common.domain.vo.MemberId;
import com.traveladvisor.memberserver.service.datasource.member.entity.MemberEntity;
import com.traveladvisor.memberserver.service.domain.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberDatasourceMapper {

    /**
     * MemberEntity -> Member
     *
     * @param memberEntity
     * @return member
     */
    public Member toDomain(MemberEntity memberEntity) {
        return Member.builder()
                .memberId(new MemberId(memberEntity.getId()))
                .email(memberEntity.getEmail())
                .nickname(memberEntity.getNickname())
                .firstName(memberEntity.getFirstName())
                .lastName(memberEntity.getLastName())
                .contactNumber(memberEntity.getContactNumber())
                .gender(memberEntity.getGender())
                .build();
    }

    /**
     * Member -> MemberEntity
     *
     * @param member
     * @return memberEntity
     */
    public MemberEntity toEntity(Member member) {
        return MemberEntity.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .contactNumber(member.getContactNumber())
                .gender(member.getGender())
                .build();
    }

}
