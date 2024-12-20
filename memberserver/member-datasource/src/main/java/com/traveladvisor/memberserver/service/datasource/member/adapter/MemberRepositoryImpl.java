package com.traveladvisor.memberserver.service.datasource.member.adapter;

import com.traveladvisor.member.service.datasource.member.mapper.MemberDatasourceMapper;
import com.traveladvisor.member.service.datasource.member.repository.MemberJpaRepository;
import com.traveladvisor.member.service.domain.entity.Member;
import com.traveladvisor.member.service.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;
    private final MemberDatasourceMapper memberDatasourceMapper;

    @Override
    public Member save(Member member) {
        return memberDatasourceMapper.toDomain(
                memberJpaRepository.save(memberDatasourceMapper.toEntity(member)));
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email).map(memberDatasourceMapper::toDomain);
    }

}
