package com.traveladvisor.bookingserver.service.datasource.booking.adapter;

import com.traveladvisor.bookingserver.service.datasource.booking.mapper.BookingDatasourceMapper;
import com.traveladvisor.bookingserver.service.datasource.booking.repository.BookingJpaRepository;
import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.BookingRepository;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.TraceId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class BookingRepositoryImpl implements BookingRepository {

    private final BookingJpaRepository bookingJpaRepository;
    private final BookingDatasourceMapper bookingDatasourceMapper;

    @Override
    public Booking save(Booking booking) {
        return bookingDatasourceMapper.toDomain(
                bookingJpaRepository.save(bookingDatasourceMapper.toEntity(booking)));
    }

    @Override
    public Optional<Booking> findById(BookingId bookingId) {
        return bookingJpaRepository.findById(bookingId.getValue())
                .map(bookingDatasourceMapper::toDomain);
    }

    @Override
    public Optional<Booking> findByTraceId(TraceId traceId) {
        return bookingJpaRepository.findByTraceId(traceId.getValue())
                .map(bookingDatasourceMapper::toDomain);
    }

}
