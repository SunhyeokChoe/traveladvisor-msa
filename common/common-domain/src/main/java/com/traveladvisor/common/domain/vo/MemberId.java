package com.traveladvisor.common.domain.vo;

import java.util.UUID;

public class MemberId extends DomainEntityId<UUID> {

    public MemberId(UUID value) {
        super(value);
    }

}
