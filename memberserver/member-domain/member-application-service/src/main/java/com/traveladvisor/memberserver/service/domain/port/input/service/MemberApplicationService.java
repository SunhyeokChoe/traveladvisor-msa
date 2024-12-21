package com.traveladvisor.memberserver.service.domain.port.input.service;

import com.traveladvisor.memberserver.service.domain.dto.command.CreateMemberCommand;
import com.traveladvisor.memberserver.service.domain.dto.command.CreateMemberResponse;
import com.traveladvisor.memberserver.service.domain.dto.query.QueryMemberResponse;
import jakarta.validation.Valid;

public interface MemberApplicationService {

    /**
     * email과 일치하는 회원을 조회합니다.
     *
     * @param email
     * @return QueryMemberResponse
     */
    QueryMemberResponse queryMember(String email);

    /**
     * 회원을 생성합니다.
     *
     * @param createMemberCommand
     * @param correlationId
     * @return
     */
    CreateMemberResponse createMember(@Valid CreateMemberCommand createMemberCommand, String correlationId);

}
