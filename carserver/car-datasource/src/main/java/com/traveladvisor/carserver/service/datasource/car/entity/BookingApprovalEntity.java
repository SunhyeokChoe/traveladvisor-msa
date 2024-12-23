package com.traveladvisor.carserver.service.datasource.car.entity;

import com.traveladvisor.common.domain.vo.CarBookingApprovalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.UUID;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "booking_approval", schema = "car")
@Entity
public class BookingApprovalEntity {

    @Id
    private UUID id;

    @Column(name = "car_offers_id")
    private Long carOfferId;

    @Column(name = "booking_id")
    private UUID bookingId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CarBookingApprovalStatus status;

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
