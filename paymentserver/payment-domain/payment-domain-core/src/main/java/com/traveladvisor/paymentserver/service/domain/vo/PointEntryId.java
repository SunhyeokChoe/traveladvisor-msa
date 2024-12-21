package com.traveladvisor.paymentserver.service.domain.vo;

import com.traveladvisor.common.domain.vo.DomainEntityId;

import java.util.UUID;

public class PointEntryId extends DomainEntityId<UUID> {

    public PointEntryId(UUID value) {
        super(value);
    }

}
