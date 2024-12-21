package com.traveladvisor.bookingserver.service.domain.mapper;

import com.traveladvisor.bookingserver.service.domain.dto.command.CreateBookingCommand;
import com.traveladvisor.bookingserver.service.domain.dto.command.CreateBookingResponse;
import com.traveladvisor.bookingserver.service.domain.dto.query.QueryBookingResponse;
import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.bookingserver.service.domain.event.BookingCreatedEvent;
import com.traveladvisor.common.domain.event.booking.BookingCreatedEventPayload;
import com.traveladvisor.common.domain.vo.*;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    /**
     * CreateBookingCommand -> Booking
     *
     * @param createBookingCommand 예약 생성 요청 DTO
     * @return Booking 도메인 엔터티
     */
    public Booking toBooking(CreateBookingCommand createBookingCommand) {
        return Booking.builder()
                .memberEmail(createBookingCommand.email())
                .hotelOfferId(new HotelOfferId(createBookingCommand.hotelOfferId()))
                .flightOfferId(new FlightOfferId(createBookingCommand.flightOfferId()))
                .carOfferId(new CarOfferId(createBookingCommand.carOfferId()))
                .totalPrice(new Money(createBookingCommand.totalPrice()))
                .failureMessages(null)
                .build();
    }

    /**
     * Booking -> CreateBookingResponse
     *
     * @param booking         예약서 정보
     * @param responseMessage 응답 메시지
     * @return CreateBookingResponse
     */
    public CreateBookingResponse toCreateBookingResponse(Booking booking, String responseMessage) {
        return new CreateBookingResponse(
                booking.getTraceId().getValue(),
                booking.getBookingStatus(),
                responseMessage
        );
    }

    /**
     * BookingCreatedEvent -> BookingCreatedEventPayload
     *
     * @param bookingCreatedEvent 예약서 생성 완료 이벤트
     * @return BookingCreatedEventPayload
     */
    public BookingCreatedEventPayload toBookingCreatedEventPayload(BookingCreatedEvent bookingCreatedEvent) {
        return BookingCreatedEventPayload.builder()
                .memberEmail(bookingCreatedEvent.getBooking().getMemberEmail())
                .bookingId(bookingCreatedEvent.getBooking().getId().getValue().toString())
                .totalPrice(bookingCreatedEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(bookingCreatedEvent.getCreatedAt())
                .hotelBookingStatus(BookingStatus.PENDING.name())
                .build();
    }

    public QueryBookingResponse toQueryBookingResponse(Booking booking) {
        return new QueryBookingResponse(
                booking.getTraceId().getValue(),
                booking.getBookingStatus(),
                booking.getFailureMessages());
    }

}
