package com.traveladvisor.paymentserver.service.datasource.event.entity;

import com.traveladvisor.common.datasource.common.entity.BaseAuditEntity;
import com.traveladvisor.common.domain.vo.EventActionType;
import com.traveladvisor.paymentserver.service.domain.vo.EventRewardType;
import com.traveladvisor.paymentserver.service.domain.vo.EventStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "events", schema = "event")
@Entity
public class EventEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_description")
    private String eventDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type")
    private EventActionType actionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type")
    private EventRewardType rewardType;

    @Column(name = "reward_value")
    private String rewardValue;

    @Column(name = "start_date")
    private ZonedDateTime startDate;

    @Column(name = "end_date")
    private ZonedDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EventStatus status;

}
