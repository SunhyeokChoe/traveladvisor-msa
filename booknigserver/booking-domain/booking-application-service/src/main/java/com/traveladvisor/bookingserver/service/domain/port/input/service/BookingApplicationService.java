package com.traveladvisor.bookingserver.service.domain.port.input.service;

import com.traveladvisor.bookingserver.service.domain.dto.command.CreateBookingCommand;
import com.traveladvisor.bookingserver.service.domain.dto.command.CreateBookingResponse;
import com.traveladvisor.bookingserver.service.domain.dto.query.QueryBookingResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface BookingApplicationService {

    /**
     * 호텔/항공권/차량 예약을 한 번에 진행합니다.
     *
     * @param createBookingCommand
     * @param correlationId
     * @return CreateBookingResponse
     */
    CreateBookingResponse createBooking(@Valid CreateBookingCommand createBookingCommand, String correlationId);

    /**
     * 호텔/항공권/차량 예약 진행 상황을 조회합니다.
     *
     * @param traceId 예약 조회를 위한 추적 ID
     * @return QueryBookingResponse
     */
    QueryBookingResponse queryBooking(UUID traceId);

}
