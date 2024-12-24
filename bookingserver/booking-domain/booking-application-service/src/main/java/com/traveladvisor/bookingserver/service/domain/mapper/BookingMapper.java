package com.traveladvisor.bookingserver.service.domain.mapper;

import com.traveladvisor.bookingserver.service.domain.dto.command.CreateBookingCommand;
import com.traveladvisor.bookingserver.service.domain.dto.command.CreateBookingResponse;
import com.traveladvisor.bookingserver.service.domain.dto.query.QueryBookingResponse;
import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.bookingserver.service.domain.event.BookingCancelledEvent;
import com.traveladvisor.bookingserver.service.domain.event.BookingCreatedEvent;
import com.traveladvisor.bookingserver.service.domain.event.FlightBookedEvent;
import com.traveladvisor.bookingserver.service.domain.event.HotelBookedEvent;
import com.traveladvisor.common.domain.event.booking.BookingCancelledEventPayload;
import com.traveladvisor.common.domain.event.booking.BookingCreatedEventPayload;
import com.traveladvisor.common.domain.event.booking.FlightBookedEventPayload;
import com.traveladvisor.common.domain.event.booking.HotelBookedEventPayload;
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
                .hotelBookingStatus(HotelBookingStatus.PENDING.name())
                .build();
    }

    public QueryBookingResponse toQueryBookingResponse(Booking booking) {
        return new QueryBookingResponse(
                booking.getTraceId().getValue(),
                booking.getBookingStatus(),
                booking.getFailureMessages());
    }

    public HotelBookedEventPayload toHotelBookedEventPayload(HotelBookedEvent hotelBookedEvent) {
        return HotelBookedEventPayload.builder()
                .id(hotelBookedEvent.getBooking().getId().getValue().toString())
                .bookingId(hotelBookedEvent.getBooking().getId().getValue().toString())
                .flightOfferId(hotelBookedEvent.getBooking().getFlightOfferId().getValue().toString())
                .memberEmail(hotelBookedEvent.getBooking().getMemberEmail())
                .totalPrice(hotelBookedEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(hotelBookedEvent.getCreatedAt())
                .flightBookingStatus(hotelBookedEvent.getBooking().getBookingStatus().name())
                .build();
    }

    public BookingCancelledEventPayload toBookingCancelledEvent(BookingCancelledEvent bookingCancelledEvent) {
        return BookingCancelledEventPayload.builder()
                .memberEmail(bookingCancelledEvent.getBooking().getMemberEmail())
                .bookingId(bookingCancelledEvent.getBooking().getId().getValue().toString())
                .totalPrice(bookingCancelledEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(bookingCancelledEvent.getCreatedAt())
                .hotelBookingStatus(HotelBookingStatus.PENDING.name())
                .build();
    }

    public FlightBookedEventPayload toFlightBookedEventPayload(FlightBookedEvent flightBookedEvent) {
        return FlightBookedEventPayload.builder()
                .id(flightBookedEvent.getBooking().getId().getValue().toString())
                .bookingId(flightBookedEvent.getBooking().getId().getValue().toString())
                .carOfferId(flightBookedEvent.getBooking().getCarOfferId().getValue().toString())
                .memberEmail(flightBookedEvent.getBooking().getMemberEmail())
                .totalPrice(flightBookedEvent.getBooking().getTotalPrice().getAmount())
                .createdAt(flightBookedEvent.getCreatedAt())
                .carBookingStatus(flightBookedEvent.getBooking().getBookingStatus().name())
                .build();
    }

}
