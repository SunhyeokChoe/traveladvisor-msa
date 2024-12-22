package com.traveladvisor.batchserver.service.domain.port.output.repository;

import com.traveladvisor.batchserver.service.domain.entity.Hotel;

public interface HotelRepository {

    Hotel save(Hotel hotel);

    void flush();

}
