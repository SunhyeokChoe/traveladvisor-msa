package com.traveladvisor.bookingserver.service.domain.usecase;

import com.traveladvisor.bookingserver.service.domain.dto.command.CreateBookingCommand;
import com.traveladvisor.bookingserver.service.domain.dto.command.CreateBookingResponse;
import com.traveladvisor.bookingserver.service.domain.dto.query.QueryBookingResponse;
import com.traveladvisor.bookingserver.service.domain.port.input.service.BookingApplicationService;
import com.traveladvisor.bookingserver.service.domain.usecase.command.CreateBookingCommandHandler;
import com.traveladvisor.bookingserver.service.domain.usecase.query.QueryBookingQueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Validated
@Service
public class BookingApplicationServiceImpl implements BookingApplicationService {

    private final CreateBookingCommandHandler createBookingCommandHandler;
    private final QueryBookingQueryHandler queryBookingQueryHandler;

    @Override
    public CreateBookingResponse createBooking(CreateBookingCommand createBookingCommand, String correlationId) {
        return createBookingCommandHandler.createBooking(createBookingCommand, correlationId);
    }

    @Override
    public QueryBookingResponse queryBooking(UUID traceId) {
        return queryBookingQueryHandler.queryBooking(traceId);
    }

}
