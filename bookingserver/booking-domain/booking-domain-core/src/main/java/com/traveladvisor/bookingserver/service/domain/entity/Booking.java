package com.traveladvisor.bookingserver.service.domain.entity;

import com.traveladvisor.bookingserver.service.domain.event.HotelBookedEvent;
import com.traveladvisor.bookingserver.service.domain.exception.BookingDomainCoreException;
import com.traveladvisor.common.domain.entity.AggregateRoot;
import com.traveladvisor.common.domain.vo.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Booking 도메인의 Aggregate Root입니다.
 * 예약 정보를 포함하는 핵심 엔터티입니다.
 */
public class Booking extends AggregateRoot<BookingId> {

    // 최소 예약 금액
    private final static Money MINIMUM_BOOKING_PRICE = new Money(BigDecimal.valueOf(10000));

    // 불변 (생성 시점에 초기화)
    private final String memberEmail;
    private final HotelOfferId hotelOfferId;
    private final FlightOfferId flightOfferId;
    private final CarOfferId carOfferId;
    private final Money totalPrice;

    private TraceId traceId;
    private BookingStatus bookingStatus;
    private List<String> failureMessages;

    /**
     * 예약서를 초기화 합니다.
     */
    public void initializeBooking() {
        // 검증
        validateInitialBooking();

        // 초기화
        initializeId();
        initializeTraceId();
        updateBookingStatus(BookingStatus.PENDING);
    }

    /**
     * 예약서를 공식적으로 초기화하기 전 기존 상태에 문제는 없는지 검증합니다.
     */
    private void validateInitialBooking() {
        validateId();
        validateBookingStatus();
        validateTotalPrice();
    }

    /**
     * ID가 초기화되지 않았는지 검증합니다.
     */
    private void validateId() {
        if (getId() != null) {
            throw new BookingDomainCoreException("예약서에 bookingId가 생성돼 있습니다. " +
                    "초기화는 반드시 Booking 도메인 엔터티에서 수행해야 합니다.");
        }
    }

    /**
     * 예약 상태가 초기화되지 않았는지 검증합니다.
     */
    private void validateBookingStatus() {
        if (bookingStatus != null) {
            throw new BookingDomainCoreException("예약서에 bookingStatus가 생성돼 있습니다. " +
                    "초기화는 반드시 Booking 도메인 엔터티에서 수행해야 합니다.");
        }
    }

    /**
     * 총 예약 금액이 최소 예약 금액을 충족하는지 검증합니다.
     */
    private void validateTotalPrice() {
        if (totalPrice == null || !totalPrice.isGreaterThan(MINIMUM_BOOKING_PRICE)) {
            throw new BookingDomainCoreException("총 예약 금액은 최소 예약 금액 보다 커야 합니다.");
        }
    }

    private void initializeId() {
        setId(new BookingId(UUID.randomUUID()));
    }

    private void initializeTraceId() {
        this.traceId = new TraceId(UUID.randomUUID());
    }

    public void updateBookingStatus(BookingStatus status) {
        this.bookingStatus = status;
    }

    public void updateFailureMessages(List<String> failureMessages) {
        this.failureMessages = failureMessages;
    }

    public void markAsHotelBooked() {
        this.bookingStatus = BookingStatus.HOTEL_BOOKED;
    }

    public void markAsFlightBooked() {
        this.bookingStatus = BookingStatus.FLIGHT_BOOKED;
    }

    private Booking(Builder builder) {
        super.setId(builder.bookingId);
        this.traceId = builder.traceId;
        this.memberEmail = builder.memberEmail;
        this.hotelOfferId = builder.hotelOfferId;
        this.flightOfferId = builder.flightOfferId;
        this.carOfferId = builder.carOfferId;
        this.totalPrice = builder.totalPrice;
        this.bookingStatus = builder.bookingStatus;
        this.failureMessages = builder.failureMessages;
    }

    // BEGIN: Getter
    public TraceId getTraceId() {
        return traceId;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public HotelOfferId getHotelOfferId() {
        return hotelOfferId;
    }

    public FlightOfferId getFlightOfferId() {
        return flightOfferId;
    }

    public CarOfferId getCarOfferId() {
        return carOfferId;
    }

    public Money getTotalPrice() {
        return totalPrice;
    }

    public BookingStatus getBookingStatus() {
        return bookingStatus;
    }

    public List<String> getFailureMessages() {
        return failureMessages;
    }
    // END: Getter

    // BEGIN: Builder
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private BookingId bookingId;
        private String memberEmail;
        private HotelOfferId hotelOfferId;
        private FlightOfferId flightOfferId;
        private CarOfferId carOfferId;
        private Money totalPrice;
        private TraceId traceId;
        private BookingStatus bookingStatus;
        private List<String> failureMessages;

        private Builder() {}

        public Builder id(BookingId bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder traceId(TraceId traceId) {
            this.traceId = traceId;
            return this;
        }

        public Builder memberEmail(String memberEmail) {
            this.memberEmail = memberEmail;
            return this;
        }

        public Builder hotelOfferId(HotelOfferId hotelOfferId) {
            this.hotelOfferId = hotelOfferId;
            return this;
        }

        public Builder flightOfferId(FlightOfferId flightOfferId) {
            this.flightOfferId = flightOfferId;
            return this;
        }

        public Builder carOfferId(CarOfferId carOfferId) {
            this.carOfferId = carOfferId;
            return this;
        }

        public Builder totalPrice(Money totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        public Builder bookingStatus(BookingStatus bookingStatus) {
            this.bookingStatus = bookingStatus;
            return this;
        }

        public Builder failureMessages(List<String> failureMessages) {
            this.failureMessages = failureMessages;
            return this;
        }

        public Booking build() {
            return new Booking(this);
        }
    }
    // END: Builder

}
