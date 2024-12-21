package com.traveladvisor.paymentserver.service.domain.usecase;

import com.traveladvisor.paymentserver.service.domain.dto.command.CreatePointEntryCommand;
import com.traveladvisor.paymentserver.service.domain.dto.command.CreateRewardCommand;
import com.traveladvisor.paymentserver.service.domain.port.input.service.PaymentApplicationService;
import com.traveladvisor.paymentserver.service.domain.usecase.command.CreatePointEntryCommandHandler;
import com.traveladvisor.paymentserver.service.domain.usecase.command.CreateRewardCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@RequiredArgsConstructor
@Validated
@Service
public class PaymentApplicationServiceImpl implements PaymentApplicationService {

    private final CreatePointEntryCommandHandler createPointEntryCommandHandler;
    private final CreateRewardCommandHandler createRewardCommandHandler;

    @Override
    public void createPointEntry(CreatePointEntryCommand createPointEntry) {
        createPointEntryCommandHandler.createPointEntry(createPointEntry);
    }

    @Override
    public void createReward(CreateRewardCommand createRewardCommand) {
        createRewardCommandHandler.createReward(createRewardCommand);
    }

}
