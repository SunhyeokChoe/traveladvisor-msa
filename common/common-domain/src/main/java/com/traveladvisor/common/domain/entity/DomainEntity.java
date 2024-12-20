package com.traveladvisor.common.domain.entity;

import java.util.Objects;

/**
 * 모든 Entity는 고유 식별자를 가지며, Entity 간의 비교는 id를 통해서만 합니다. 따라서 equals & hashCode는 id만 비교하도록 수정했습니다.
 *
 * @param <ID> Entity의 고유 식별자 타입
 */
public abstract class DomainEntity<ID> {

    private ID id;

    public ID getId() {
        return id;
    }

    // ID 는 Entity 외부에서 변경되면 안되므로 protected 로 설정합니다.
    protected void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainEntity<?> that = (DomainEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
