package com.traveladvisor.common.domain.entity;

/**
 * 상속하는 클래스 본인이 Aggregate Root(AR) 임을 알리기 위한 Marker Class 입니다.
 * 타입으로 사용하므로 내부적으로 메서드나 필드를 갖지 않습니다.
 * Aggregate Root 는 고유 식별자를 가지므로 DomainEntity 를 상속합니다.
 *
 * Aggregate Root의 역할
 * - 하위 엔터티들의 상태들을 관리하고, 일관된 상태로 유지하기 위해 항상 엄격한 유효성 검사를 하는 책임을 갖습니다.
 *
 * @param <ID> Aggregate Root 의 고유 식별자 타입
 */
public abstract class AggregateRoot<ID> extends DomainEntity<ID> {

    /* *** NO-OP: 이곳에 필드나 메서드를 추가하시면 안됩니다. *** */

}
