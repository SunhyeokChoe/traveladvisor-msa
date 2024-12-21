package com.traveladvisor.paymentserver.service.datasource.payment.mapper;

import com.traveladvisor.common.domain.vo.MemberId;
import com.traveladvisor.common.domain.vo.Money;
import com.traveladvisor.paymentserver.service.datasource.payment.entity.PointEntryEntity;
import com.traveladvisor.paymentserver.service.domain.entity.PointEntry;
import com.traveladvisor.paymentserver.service.domain.vo.PointEntryId;
import org.springframework.stereotype.Component;

@Component
public class PointEntryDatasourceMapper {

    /**
     * PointEntryEntity -> PointEntry
     *
     * @param pointEntryEntity 포인트 엔티티
     * @return PointEntry 도메인 객체
     */
    public PointEntry toDomain(PointEntryEntity pointEntryEntity) {
        return PointEntry.builder()
                .pointEntryId(new PointEntryId(pointEntryEntity.getId()))
                .memberId(new MemberId(pointEntryEntity.getMemberId()))
                .totalPointAmount(new Money(pointEntryEntity.getTotalPointAmount()))
                .build();
    }

    /**
     * PointEntry -> PointEntryEntity
     *
     * @param pointEntry 포인트 도메인 객체
     * @return PointEntryEntity 객체
     */
    public PointEntryEntity toEntity(PointEntry pointEntry) {
        return PointEntryEntity.builder()
                .id(pointEntry.getId() != null ? pointEntry.getId().getValue() : null)
                .memberId(pointEntry.getMemberId() != null ? pointEntry.getMemberId().getValue() : null)
                .totalPointAmount(pointEntry.getTotalPointAmount() != null ? pointEntry.getTotalPointAmount().getAmount() : null)
                .build();
    }

}
