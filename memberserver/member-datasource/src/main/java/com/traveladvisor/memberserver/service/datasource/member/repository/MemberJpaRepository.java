package com.traveladvisor.memberserver.service.datasource.member.repository;

import com.traveladvisor.memberserver.service.datasource.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberJpaRepository extends JpaRepository<MemberEntity, UUID> {

    Optional<MemberEntity> findByEmail(String email);

}
