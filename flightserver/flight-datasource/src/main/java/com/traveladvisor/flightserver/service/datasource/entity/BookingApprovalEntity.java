package com.traveladvisor.flightserver.service.datasource.entity;

import com.traveladvisor.common.domain.vo.FlightBookingApprovalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "booking_approval", schema = "flight")
@Entity
public class BookingApprovalEntity {

    @Id
    private UUID id;

    @Column(name = "hotel_offers_id")
    private Long hotelOfferId;

    @Column(name = "booking_id")
    private UUID bookingId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FlightBookingApprovalStatus status;

    @Version
    @Column(name = "version")
    private int version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingApprovalEntity that = (BookingApprovalEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
