package com.traveladvisor.paymentserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.DomainEntity;
import com.traveladvisor.common.domain.vo.MemberId;
import com.traveladvisor.common.domain.vo.Money;
import com.traveladvisor.paymentserver.service.domain.vo.PointEntryId;

public class PointEntry extends DomainEntity<PointEntryId> {

    private final MemberId memberId;
    private Money totalPointAmount;

    /**
     * 포인트 계좌에 포인트를 적립합니다.
     *
     * @param amount
     */
    public void addPointAmount(Money amount) {
        totalPointAmount = totalPointAmount.add(amount);
    }

    /**
     * 포인트 계좌에 포인트를 차감합니다.
     *
     * @param amount
     */
    public void subtractPointAmount(Money amount) {
        totalPointAmount = totalPointAmount.subtract(amount);
    }

    private PointEntry(Builder builder) {
        super.setId(builder.pointEntryId);
        memberId = builder.memberId;
        totalPointAmount = builder.totalPointAmount;
    }

    // BEGIN: Getter
    public MemberId getMemberId() {
        return memberId;
    }

    public Money getTotalPointAmount() {
        return totalPointAmount;
    }
    // END: Getter

    // BEGIN: Builder
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private PointEntryId pointEntryId;
        private MemberId memberId;
        private Money totalPointAmount;

        private Builder() {
        }

        public Builder pointEntryId(PointEntryId val) {
            pointEntryId = val;
            return this;
        }

        public Builder memberId(MemberId val) {
            memberId = val;
            return this;
        }

        public Builder totalPointAmount(Money val) {
            totalPointAmount = val;
            return this;
        }

        public PointEntry build() {
            return new PointEntry(this);
        }
    }
    // END: Builder

}
