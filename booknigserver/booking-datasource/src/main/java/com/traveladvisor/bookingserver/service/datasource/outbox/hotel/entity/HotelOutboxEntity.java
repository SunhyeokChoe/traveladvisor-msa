package com.traveladvisor.bookingserver.service.datasource.outbox.hotel.entity;

import com.traveladvisor.common.datasource.common.entity.BaseZonedDateTimeEntity;
import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import com.traveladvisor.common.domain.vo.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "hotel_outbox", schema = "booking")
@Entity
public class HotelOutboxEntity extends BaseZonedDateTimeEntity {

    @Id
    private UUID id;

    @Column(name = "saga_action_name")
    private UUID sagaActionId;

    @Column(name = "event_type")
    private String eventType;

    @Lob
    @Column(name = "event_payload")
    private String eventPayload;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "outbox_status")
    private OutboxStatus outboxStatus = OutboxStatus.STARTED;

    @Enumerated(EnumType.STRING)
    @Column(name = "saga_action_status")
    private SagaActionStatus sagaActionStatus = SagaActionStatus.STARTED;

    private ZonedDateTime completedAt;

    @Version
    @Column(name = "version")
    private int version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HotelOutboxEntity that = (HotelOutboxEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
