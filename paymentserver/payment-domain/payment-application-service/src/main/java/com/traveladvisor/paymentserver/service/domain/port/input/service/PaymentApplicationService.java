package com.traveladvisor.paymentserver.service.domain.port.input.service;

import com.traveladvisor.paymentserver.service.domain.dto.command.CreatePointEntryCommand;
import com.traveladvisor.paymentserver.service.domain.dto.command.CreateRewardCommand;
import jakarta.validation.Valid;

public interface PaymentApplicationService {

    /**
     * 회원의 포인트 계좌를 생성합니다.
     *
     * @param createPointEntry
     */
    void createPointEntry(@Valid CreatePointEntryCommand createPointEntry);

    /**
     * 진행 중인 이벤트가 존재하는지 확인하고 리워드를 제공합니다.
     *
     * @param createRewardCommand
     */
    void createReward(@Valid CreateRewardCommand createRewardCommand);

}
