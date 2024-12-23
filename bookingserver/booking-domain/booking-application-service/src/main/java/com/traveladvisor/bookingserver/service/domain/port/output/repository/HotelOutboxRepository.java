package com.traveladvisor.bookingserver.service.domain.port.output.repository;

import com.traveladvisor.bookingserver.service.domain.outbox.model.hotel.HotelOutbox;
import com.traveladvisor.common.domain.saga.SagaActionStatus;

import java.util.Optional;
import java.util.UUID;

public interface HotelOutboxRepository {

    HotelOutbox save(HotelOutbox hotelOutbox);

    Optional<HotelOutbox> findByEventTypeAndSagaActionIdAndSagaActionStatusIn(String eventType,
                                                                              UUID sagaActionId,
                                                                              SagaActionStatus... sagaActionStatuses);

}
