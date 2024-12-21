package com.traveladvisor.paymentserver.service.domain.usecase;

import com.traveladvisor.common.domain.vo.EventActionType;
import com.traveladvisor.common.domain.vo.MemberId;
import com.traveladvisor.common.domain.vo.Money;
import com.traveladvisor.paymentserver.service.domain.entity.Event;
import com.traveladvisor.paymentserver.service.domain.entity.PointEntry;
import com.traveladvisor.paymentserver.service.domain.exception.PaymentApplicationServiceException;
import com.traveladvisor.paymentserver.service.domain.port.output.repository.EventRepository;
import com.traveladvisor.paymentserver.service.domain.port.output.repository.PointEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 이벤트 리워드 타입에 따른 리워드 선택적 지급 클래스입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class RewardDistributeHelper {

    private final EventRepository eventRepository;
    private final PointEntryRepository pointEntryRepository;

    /**
     * 회원에게 이벤트 액션 타입에 맞는 리워드를 지급합니다.
     *
     * @param memberId        회원 ID
     * @param eventActionType 이벤트 액션 타입
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void distributeReward(MemberId memberId, EventActionType eventActionType) {
        // 이벤트를 조회합니다.
        List<Event> events = eventRepository.findByActionType(eventActionType);
        if (events.isEmpty()) {
            throw new PaymentApplicationServiceException("해당 액션 타입(" + eventActionType.name() +
                    ")에 맞는 이벤트가 존재하지 않습니다.");
        }

        // 각 이벤트에 대해 리워드를 지급합니다.
        events.forEach(event -> {
            switch (event.getRewardType()) {
                case POINT -> rewardMemberPoints(memberId, event);
                case GIFT, VOUCHER, DISCOUNT -> {
                    log.info("리워드 타입 '{}'은 현재 지원하지 않습니다. 추후 지원 예정입니다.", event.getRewardType());
                    throw new PaymentApplicationServiceException("리워드 타입 '" + event.getRewardType() + "'은 현재 지원하지 " +
                            "않습니다. 추후 지원 예정입니다.");
                }
                default -> throw new PaymentApplicationServiceException("지원하지 않는 리워드 타입입니다: " + event.getRewardType());
            }
        });
    }

    /**
     * 포인트 리워드를 지급합니다.
     *
     * @param memberId 회원 ID
     * @param event    이벤트 정보
     */
    private void rewardMemberPoints(MemberId memberId, Event event) {
        // 포인트 계좌를 확인합니다.
        PointEntry pointEntry = pointEntryRepository.findById(memberId)
                .orElseThrow(() -> new PaymentApplicationServiceException("포인트 계좌가 존재하지 않습니다."));

        // 포인트를 지급합니다.
        BigDecimal rewardValue = new BigDecimal(event.getRewardValue());
        pointEntry.addPointAmount(new Money(rewardValue));

        // 포인트 계좌를 저장합니다.
        pointEntryRepository.save(pointEntry);

        log.info("회원 ID: {}에게 '{}' 이벤트에 따라 {} 포인트가 지급되었습니다.",
                memberId.getValue(), event.getEventName(), rewardValue);
    }

}
