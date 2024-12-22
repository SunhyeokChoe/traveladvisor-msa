package com.traveladvisor.common.domain.vo;

import com.traveladvisor.common.domain.exception.DomainException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * 도매인 엔터티 내 각 필드가 어떤 목적으로 사용되는지 명확해야 합니다.
 * 예를 들어 금액의 경우 Money 라고 하는 VO로 만들어 의미를 부여하고,
 * 내부에서 필드를 BigDecimal로 설정하면 손쉽게 금액으로써 개념을 사용할 수 있습니다.
 * 그리고 그 VO 안에서 해당 VO가 어떻게 사용되어야 하는지 메서드를 구현하면 VO의
 * 목적과 개념을 보다 명확하게 외부에 알릴 수 있습니다.
 */
public class Money {

    private final BigDecimal amount;

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isGreaterThanZero() {
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Money money) {
        return this.amount != null && this.amount.compareTo(money.getAmount()) > 0;
    }

    public Money add(Money money) {
        return new Money(setScale(this.amount.add(money.getAmount())));
    }

    public Money subtract(Money money) {
        BigDecimal result = this.amount.subtract(money.getAmount());
        if (result.compareTo(BigDecimal.ZERO) < 0) {
            throw new DomainException("금액은 0보다 작을 수 없습니다.");
        }

        return new Money(setScale(this.amount.subtract(money.getAmount())));
    }

    public Money multiply(int multiplier) {
        return new Money(setScale(this.amount.multiply(new BigDecimal(multiplier))));
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    /**
     * 10진수에서 1을 3으로 나누면 0.33333333... 으로 반복되므로 정확한 숫자를 표현할 수 없습니다.
     * 이진법에도 이 성질을 가진 숫자가 있습니다. 2진법에서 7을 10으로 나누는 상황에서 발생합니다.
     * 7 / 10 = 111 / 1010 = 0.10110011001100... 처럼 1100이 반복됩니다.
     * 자바는 반복되는 순환소수값을 표현하기 위해 사용 가능한 비트만을 사용합니다. 예를 들어 자료형 Double의 경우 다음과 같습니다.
     * e.g., Double 0.7 = 111/1010 = 0.10110011001100..11 (52bit) -> 52번째 비트 이후는 모두 무시됩니다.
     * 그러므로 숫자를 계속 더하다 보면 총 오차는 점점 더 커지게 될 것입니다.
     * 이러한 증가하는 오차를 줄이기 위해 HALF_EVEN 즉, 반올림 모드를 사용합니다.
     * 이는 누적 오차를 통계적으로 최소화하는 효과를 갖습니다.
     * 이것이 바로 Money VO에 setScale 메서드를 생성한 이유입니다.
     */
    private BigDecimal setScale(BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }

}
