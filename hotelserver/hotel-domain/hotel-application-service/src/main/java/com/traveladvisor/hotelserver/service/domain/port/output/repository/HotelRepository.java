package com.traveladvisor.hotelserver.service.domain.port.output.repository;

import com.traveladvisor.hotelserver.service.domain.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HotelRepository {

    Page<Hotel> findAll(Pageable pageable);

}
