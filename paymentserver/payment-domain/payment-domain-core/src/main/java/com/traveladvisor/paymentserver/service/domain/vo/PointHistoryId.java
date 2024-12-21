package com.traveladvisor.paymentserver.service.domain.vo;

import com.traveladvisor.common.domain.vo.DomainEntityId;

import java.util.UUID;

public class PointHistoryId extends DomainEntityId<UUID> {

    public PointHistoryId(UUID value) {
        super(value);
    }

}
