package com.traveladvisor.bookingserver.service.domain.port.output.repository;

import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.TraceId;

import java.util.Optional;

public interface BookingRepository {

    Booking save(Booking booking);

    Optional<Booking> findById(BookingId bookingId);

    Optional<Booking> findByTraceId(TraceId traceId);

}
