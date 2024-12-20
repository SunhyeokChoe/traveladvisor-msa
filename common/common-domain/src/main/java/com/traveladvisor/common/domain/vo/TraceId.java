package com.traveladvisor.common.domain.vo;

import java.util.UUID;

public class TraceId extends DomainEntityId<UUID> {

    public TraceId(UUID value) {
        super(value);
    }

}
