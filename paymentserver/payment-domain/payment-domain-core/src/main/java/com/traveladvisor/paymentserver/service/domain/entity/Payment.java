package com.traveladvisor.paymentserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.AggregateRoot;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.MemberId;
import com.traveladvisor.common.domain.vo.Money;
import com.traveladvisor.common.domain.vo.PaymentStatus;
import com.traveladvisor.paymentserver.service.domain.vo.PaymentId;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static com.traveladvisor.common.domain.constant.common.DomainConstants.UTC;

public class Payment extends AggregateRoot<PaymentId> {

    private final BookingId bookingId;
    private final MemberId memberId;
    private final Money price;

    private PaymentStatus paymentStatus;
    private ZonedDateTime createdAt;

    /**
     * 결제 초기화
     */
    public void initializePayment() {
        super.setId(new PaymentId(UUID.randomUUID()));
        createdAt = ZonedDateTime.now(ZoneId.of(UTC));
    }

    /**
     * 결제 유효성 검증
     *
     * @param failureMessages 결제가 유효하지 않을 경우 이 필드에 예외 메시지를 담아주세요.
     */
    public void validatePayment(List<String> failureMessages) {
        if (price == null || !price.isGreaterThanZero()) {
            failureMessages.add("결제 총액은 반드시 0보다 커야 합니다.");
        }
    }

    /**
     * 결제 진행 상태를 업데이트합니다.
     *
     * @param paymentStatus
     */
    public void updateStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    private Payment(Builder builder) {
        super.setId(builder.paymentId);
        bookingId = builder.bookingId;
        memberId = builder.memberId;
        price = builder.price;
        paymentStatus = builder.paymentStatus;
        createdAt = builder.createdAt;
    }

    // BEGIN: Getter
    public BookingId getBookingId() {
        return bookingId;
    }

    public MemberId getMemberId() {
        return memberId;
    }

    public Money getPrice() {
        return price;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
    // END: Getter

    // BEGIN: Builder
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private PaymentId paymentId;
        private BookingId bookingId;
        private MemberId memberId;
        private Money price;
        private PaymentStatus paymentStatus;
        private ZonedDateTime createdAt;

        private Builder() {
        }

        public Builder paymentId(PaymentId val) {
            paymentId = val;
            return this;
        }

        public Builder bookingId(BookingId val) {
            bookingId = val;
            return this;
        }

        public Builder memberId(MemberId val) {
            memberId = val;
            return this;
        }

        public Builder price(Money val) {
            price = val;
            return this;
        }

        public Builder paymentStatus(PaymentStatus val) {
            paymentStatus = val;
            return this;
        }

        public Builder createdAt(ZonedDateTime val) {
            createdAt = val;
            return this;
        }

        public Payment build() {
            return new Payment(this);
        }
    }
    // END: Builder

}
