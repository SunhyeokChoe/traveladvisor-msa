package com.traveladvisor.memberserver.service.domain.repository;

import com.traveladvisor.memberserver.service.domain.entity.Member;

import java.util.Optional;

public interface MemberRepository {

    Member save(Member member);

    Optional<Member> findByEmail(String email);

}
