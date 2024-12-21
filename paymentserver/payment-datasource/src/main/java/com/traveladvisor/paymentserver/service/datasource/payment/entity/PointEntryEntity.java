package com.traveladvisor.paymentserver.service.datasource.payment.entity;

import com.traveladvisor.common.datasource.common.entity.BaseAuditEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "point_entries", schema = "payment")
@Entity
public class PointEntryEntity extends BaseAuditEntity {

    @Id @Column(name = "id")
    private UUID id;

    @Column(name = "member_id")
    private UUID memberId;

    @Column(name = "total_point_amount")
    private BigDecimal totalPointAmount;

}
