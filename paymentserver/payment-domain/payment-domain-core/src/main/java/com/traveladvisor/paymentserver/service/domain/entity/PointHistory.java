package com.traveladvisor.paymentserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.DomainEntity;
import com.traveladvisor.common.domain.vo.MemberId;
import com.traveladvisor.common.domain.vo.Money;
import com.traveladvisor.paymentserver.service.domain.vo.PointHistoryId;
import com.traveladvisor.paymentserver.service.domain.vo.PointTranType;

public class PointHistory extends DomainEntity<PointHistoryId> {

    private final MemberId memberId;
    private final Money amount;
    private final PointTranType pointTranType;

    private PointHistory(Builder builder) {
        super.setId(builder.pointHistoryId);
        memberId = builder.memberId;
        amount = builder.amount;
        pointTranType = builder.pointTranType;
    }

    // BEGIN: Getter
    public MemberId getMemberId() {
        return memberId;
    }

    public Money getAmount() {
        return amount;
    }

    public PointTranType getPointTranType() {
        return pointTranType;
    }
    // END: Getter

    // BEGIN: Builder
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private PointHistoryId pointHistoryId;
        private MemberId memberId;
        private Money amount;
        private PointTranType pointTranType;

        private Builder() {
        }

        public Builder pointHistoryId(PointHistoryId val) {
            pointHistoryId = val;
            return this;
        }

        public Builder memberId(MemberId val) {
            memberId = val;
            return this;
        }

        public Builder amount(Money val) {
            amount = val;
            return this;
        }

        public Builder pointTranType(PointTranType val) {
            pointTranType = val;
            return this;
        }

        public PointHistory build() {
            return new PointHistory(this);
        }
    }
    // END: Builder

}
