package com.traveladvisor.common.domain.vo;

import java.util.UUID;

public class BookingId extends DomainEntityId<UUID> {

    public BookingId(UUID value) {
        super(value);
    }

}
