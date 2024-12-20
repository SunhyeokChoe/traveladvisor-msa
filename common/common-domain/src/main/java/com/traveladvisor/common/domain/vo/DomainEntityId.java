package com.traveladvisor.common.domain.vo;

import java.util.Objects;

/**
 * DomainEntityId는 도메인의 ID를 나타내는 추상 클래스입니다.
 *
 * @param <ID> DomainEntity의 ID 타입
 */
public abstract class DomainEntityId<ID> {

    private final ID id;

    protected DomainEntityId(ID id) {
        this.id = id;
    }

    public ID getValue() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainEntityId<?> that = (DomainEntityId<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
