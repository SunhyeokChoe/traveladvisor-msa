package com.traveladvisor.bookingserver.service.datasource.booking.mapper;

import com.traveladvisor.bookingserver.service.datasource.booking.entity.BookingEntity;
import com.traveladvisor.bookingserver.service.domain.entity.Booking;
import com.traveladvisor.common.domain.vo.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;

@Component
public class BookingDatasourceMapper {

    /**
     * BookingEntity -> Booking
     *
     * @param bookingEntity BookingEntity 객체
     * @return Booking 도메인 객체
     */
    public Booking toDomain(BookingEntity bookingEntity) {
        return Booking.builder()
                .id(new BookingId(bookingEntity.getId()))
                .traceId(new TraceId(bookingEntity.getTraceId()))
                .memberEmail(bookingEntity.getMemberEmail())
                .hotelOfferId(new HotelOfferId(bookingEntity.getHotelOfferId()))
                .flightOfferId(new FlightOfferId(bookingEntity.getFlightOfferId()))
                .carOfferId(new CarOfferId(bookingEntity.getCarOfferId()))
                .totalPrice(new Money(bookingEntity.getTotalPrice()))
                .bookingStatus(bookingEntity.getBookingStatus())
                .failureMessages(bookingEntity.getFailureMessages().isEmpty() ?
                        new ArrayList<>() : new ArrayList<>(Arrays.asList(bookingEntity.getFailureMessages()
                        .split(","))))
                .build();
    }

    /**
     * Booking -> BookingEntity
     *
     * @param booking Booking 도메인 객체
     * @return BookingEntity 객체
     */
    public BookingEntity toEntity(Booking booking) {
        return BookingEntity.builder()
                .id(booking.getId().getValue())
                .traceId(booking.getTraceId().getValue())
                .memberEmail(booking.getMemberEmail())
                .hotelOfferId(booking.getHotelOfferId().getValue())
                .flightOfferId(booking.getFlightOfferId().getValue())
                .carOfferId(booking.getCarOfferId().getValue())
                .totalPrice(booking.getTotalPrice().getAmount())
                .bookingStatus(booking.getBookingStatus())
                .failureMessages(booking.getFailureMessages() != null ?
                        String.join(",", booking.getFailureMessages()) : "")
                .build();
    }

}
