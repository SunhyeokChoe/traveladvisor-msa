package com.traveladvisor.bookingserver.service.domain.usecase.query;

import com.traveladvisor.bookingserver.service.domain.dto.query.QueryBookingResponse;
import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.bookingserver.service.domain.exception.BookingNotFoundException;
import com.traveladvisor.bookingserver.service.domain.mapper.BookingMapper;
import com.traveladvisor.bookingserver.service.domain.port.output.repository.BookingRepository;
import com.traveladvisor.common.domain.vo.TraceId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class QueryBookingQueryHandler {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public QueryBookingResponse queryBooking(UUID traceId) {
        Booking savedBooking = bookingRepository.findByTraceId(new TraceId(traceId))
                .orElseThrow(() -> {
                    log.error("해당 예약서를 찾을 수 없습니다. TraceId: {}", traceId.toString());
                    return new BookingNotFoundException("해당 예약서를 찾을 수 없습니다. TraceId: " +
                            traceId.toString());
                });

        return bookingMapper.toQueryBookingResponse(savedBooking);
    }

}
