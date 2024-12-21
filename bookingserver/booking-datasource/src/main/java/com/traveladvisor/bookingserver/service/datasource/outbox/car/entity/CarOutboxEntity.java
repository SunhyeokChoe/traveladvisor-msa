package com.traveladvisor.bookingserver.service.datasource.outbox.car.entity;

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
@Table(name = "car_outbox", schema = "booking")
@Entity
public class CarOutboxEntity extends BaseZonedDateTimeEntity {

    @Id
    private UUID id;

    @Column(name = "saga_action_id", nullable = false)
    private UUID sagaActionId;

    @Column(name = "event_type", nullable = false, length = 20)
    private String eventType;

    @Lob
    @Column(name = "event_payload", nullable = false)
    private String eventPayload;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false)
    private BookingStatus bookingStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "outbox_status", nullable = false)
    private OutboxStatus outboxStatus = OutboxStatus.STARTED;

    @Enumerated(EnumType.STRING)
    @Column(name = "saga_action_status", nullable = false)
    private SagaActionStatus sagaActionStatus = SagaActionStatus.STARTED;

    @Column(name = "completed_at")
    private ZonedDateTime completedAt;

    @Version
    @Column(name = "version", nullable = false)
    private int version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarOutboxEntity that = (CarOutboxEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
