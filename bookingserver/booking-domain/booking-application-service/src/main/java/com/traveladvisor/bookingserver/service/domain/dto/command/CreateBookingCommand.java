package com.traveladvisor.bookingserver.service.domain.dto.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 예약 요청 DTO 입니다.
 */
public record CreateBookingCommand(
        @NotBlank @Email
        String email,
        @NotBlank
        String nickname,
        @NotBlank
        String contactNumber,

        // 호텔 예약 정보
        Long hotelOfferId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        String roomType,
        List<String> services, // 시설 및 어매니티 등 사용하고자 하는 서비스 목록

        // 항공권 예약 정보
        Long flightOfferId,
        String departureAirport,
        String destinationAirport,
        LocalDateTime departureDateTime,
        LocalDateTime arrivalDateTime,
        String seatClass,

        // 차량 예약 정보
        Long carOfferId,
        String pickupLocation,
        String dropoffLocation,
        LocalDateTime pickupDateTime,
        LocalDateTime dropoffDateTime,
        String carType,

        // 결제 정보
        @NotNull
        BigDecimal totalPrice,
        @NotBlank
        String currency,
        @NotBlank
        String paymentMethod

) {}
