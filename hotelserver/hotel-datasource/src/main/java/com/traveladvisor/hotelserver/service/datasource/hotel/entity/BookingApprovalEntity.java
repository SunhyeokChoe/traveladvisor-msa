package com.traveladvisor.hotelserver.service.datasource.hotel.entity;

import com.traveladvisor.common.domain.vo.HotelBookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "booking_approval", schema = "hotel")
@Entity
public class BookingApprovalEntity {

    @Id
    private UUID id;

    @Column(name = "hotel_offers_id")
    private Long hotelOffersId;

    @Column(name = "booking_id")
    private UUID bookingId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private HotelBookingStatus status;

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
