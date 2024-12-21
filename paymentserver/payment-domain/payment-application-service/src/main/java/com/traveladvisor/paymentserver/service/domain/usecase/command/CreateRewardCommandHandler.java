package com.traveladvisor.paymentserver.service.domain.usecase.command;

import com.traveladvisor.common.domain.vo.MemberId;
import com.traveladvisor.paymentserver.service.domain.dto.command.CreateRewardCommand;
import com.traveladvisor.paymentserver.service.domain.usecase.RewardDistributeHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Component
public class CreateRewardCommandHandler {

    private final RewardDistributeHelper rewardDistributeHelper;

    @Transactional
    public void createReward(CreateRewardCommand createRewardCommand) {
        rewardDistributeHelper.distributeReward(
                new MemberId(createRewardCommand.memberId()), createRewardCommand.eventActionType());
    }

}
