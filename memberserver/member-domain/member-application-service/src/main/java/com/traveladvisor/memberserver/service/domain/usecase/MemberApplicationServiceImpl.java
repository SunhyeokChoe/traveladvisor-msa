package com.traveladvisor.memberserver.service.domain.usecase;

import com.traveladvisor.memberserver.service.domain.dto.command.CreateMemberCommand;
import com.traveladvisor.memberserver.service.domain.dto.command.CreateMemberResponse;
import com.traveladvisor.memberserver.service.domain.dto.query.QueryMemberResponse;
import com.traveladvisor.memberserver.service.domain.port.input.service.MemberApplicationService;
import com.traveladvisor.memberserver.service.domain.usecase.command.CreateMemberCommandHandler;
import com.traveladvisor.memberserver.service.domain.usecase.query.QueryMemberQueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@RequiredArgsConstructor
@Validated
@Service
public class MemberApplicationServiceImpl implements MemberApplicationService {

    private final QueryMemberQueryHandler queryMemberQueryHandler;
    private final CreateMemberCommandHandler createMemberCommandHandler;

    @Override
    public QueryMemberResponse queryMember(String email) {
        return queryMemberQueryHandler.queryMember(email);
    }

    @Override
    public CreateMemberResponse createMember(CreateMemberCommand createCustomerCommand, String correlationId) {
        return createMemberCommandHandler.createMember(createCustomerCommand, correlationId);
    }

}
