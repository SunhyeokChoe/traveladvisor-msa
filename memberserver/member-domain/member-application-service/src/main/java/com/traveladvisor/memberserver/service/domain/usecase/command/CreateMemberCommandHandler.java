package com.traveladvisor.memberserver.service.domain.usecase.command;

import com.traveladvisor.common.domain.vo.EventActionType;
import com.traveladvisor.member.service.domain.MemberDomainService;
import com.traveladvisor.member.service.domain.dto.command.CreateMemberCommand;
import com.traveladvisor.member.service.domain.dto.command.CreateMemberResponse;
import com.traveladvisor.member.service.domain.entity.Member;
import com.traveladvisor.member.service.domain.event.MemberCreatedEvent;
import com.traveladvisor.member.service.domain.exception.MemberApplicationServiceException;
import com.traveladvisor.member.service.domain.mapper.MemberMapper;
import com.traveladvisor.member.service.domain.port.output.client.PaymentServiceApiClient;
import com.traveladvisor.member.service.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateMemberCommandHandler {

    private final MemberDomainService memberDomainService;
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

    private final PaymentServiceApiClient paymentServiceApiClient;

    /**
     * 회원가입의 경우 회원에게 즉각 피드백을 줘야 하므로 포인트 계좌 생성을 위한 Payment 서비스와의 통신은 Feign Client로 동기식으로 수행합니다.
     *
     * @param createMemberCommand
     * @param correlationId
     * @return
     */
    @Transactional
    public CreateMemberResponse createMember(CreateMemberCommand createMemberCommand, String correlationId) {
        // 중복되는 이메일이 존재하는지 확인합니다.
        validateMemberIsExists(createMemberCommand.email());

        // 회원 및 회원 생성 완료 이벤트를 생성합니다.
        MemberCreatedEvent memberCreatedEvent =
                memberDomainService.initializeMember(memberMapper.toMember(createMemberCommand));

        // 회원을 저장합니다.
        Member savedMember = saveMember(memberCreatedEvent.getMember());

        // payment 서비스에 포인트 계좌 설계 요청을 전달합니다.
        paymentServiceApiClient.createPointEntries(correlationId, savedMember.getId().getValue());

        // Payment 서비스에 포인트 충전 요청을 전달합니다. (회원가입 이벤트)
        paymentServiceApiClient.rewardMember(
                correlationId, savedMember.getId().getValue(), EventActionType.SIGNUP);

        // 생성한 이벤트를 Publisher가 발행하도록 하고, 이 이벤트를 전달받은 Consumer 측에서 카카오톡 또는 이메일로 회원가입 완료
        // 피드백을 보내도록 구현할 수도 있습니다. Publisher는 member-message 모듈에서 작성하시면 됩니다.
//        memberMessagePublisher.publish(memberCreatedEvent);

        return memberMapper.toCreateMemberResponse(memberCreatedEvent.getMember()
                , "회원가입을 완료했습니다. 즐거운 여행 되세요!");
    }

    /**
     * 회원이 이미 등록돼 있는지 확인합니다.
     *
     * @param email 회원 ID
     */
    private void validateMemberIsExists(String email) {
        memberRepository.findByEmail(email)
                .ifPresent((member) -> {
                    throw new MemberApplicationServiceException("이미 가입된 이메일입니다.");
                });
    }

    /**
     * 회원을 저장합니다.
     *
     * @param member
     * @return 데이터베이스에 저장된 member
     */
    private Member saveMember(Member member) {
        Member savedMember = memberRepository.save(member);
        log.info("회원이 데이터베이스에 생성됐습니다. 생성된 회원 ID: {}", savedMember.getId().getValue());

        return savedMember;
    }

}
