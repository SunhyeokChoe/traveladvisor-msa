package com.traveladvisor.paymentserver.service.domain.mapper;

import com.traveladvisor.common.domain.vo.MemberId;
import com.traveladvisor.common.domain.vo.Money;
import com.traveladvisor.paymentserver.service.domain.entity.PointEntry;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class PointEntryMapper {

    /**
     * 포인트 계좌 도메인 객체를 초기화 합니다.
     *
     * @param memberId
     */
    public PointEntry toPointEntry(UUID memberId) {
        return PointEntry.builder()
                .memberId(new MemberId(memberId))
                .totalPointAmount(new Money(BigDecimal.ZERO))
                .build();
    }

}
