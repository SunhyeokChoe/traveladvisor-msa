package com.traveladvisor.bookingserver.service.domain.port.output.repository;

import com.traveladvisor.bookingserver.service.domain.outbox.model.hotel.HotelOutbox;

public interface HotelOutboxRepository {

    HotelOutbox save(HotelOutbox hotelOutbox);

}
