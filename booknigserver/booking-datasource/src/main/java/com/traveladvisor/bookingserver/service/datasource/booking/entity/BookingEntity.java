package com.traveladvisor.bookingserver.service.datasource.booking.entity;

import com.traveladvisor.common.datasource.common.entity.BaseAuditEntity;
import com.traveladvisor.common.domain.vo.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * BookingEntity는 booking 서비스에서만 사용하므로 공통으로 포함시키지 않고, booking 서비스에 위치시켰습니다.
 */
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "bookings", schema = "booking")
@Entity
public class BookingEntity extends BaseAuditEntity {

    @Id @Column(name = "id")
    private UUID id;

    @Column(name = "trace_id")
    private UUID traceId;

    @Column(name = "member_email")
    private String memberEmail;

    @Column(name = "hotel_offer_id")
    private Long hotelOfferId;

    @Column(name = "flight_offer_id")
    private Long flightOfferId;

    @Column(name = "car_offer_id")
    private Long carOfferId;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus;

    @Column(name = "failure_messages")
    private String failureMessages;

}
