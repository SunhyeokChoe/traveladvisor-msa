package com.traveladvisor.paymentserver.service.domain.vo;

import com.traveladvisor.common.domain.vo.DomainEntityId;

import java.util.UUID;

public class PaymentId extends DomainEntityId<UUID> {

    public PaymentId(UUID value) {
        super(value);
    }

}
