package com.traveladvisor.common.domain.saga;

/**
 * 이 인터페이스는 각 Saga 단계에서 구현해야 하는 정의입니다.
 * 각 사가 단계는 E 유형의 이벤트를 전달받아 로컬 트랜잭션을 처리합니다.
 *
 * 이 데모 프로젝트에서 Saga 흐름의 Orchestrator는 Booking 마이크로서비스입니다.
 * 따라서 Saga의 모든 액션을 Booking 마이크로서비스 한 곳에서 관리합니다.
 *
 * @param <E> event
 */
public interface SagaAction<E> {

    /**
     * LLT의 일부분이 되는 Saga 액션을 처리합니다.
     *
     * @param event
     */
    void process(E event);

    /**
     * 보상 트랜잭션을 처리합니다.
     * Saga 액션 수행 중 문제가 발생한 경우 문제가 발생한 Saga 액션 바로 직전의 Saga 액션에서 이를 보상합니다.
     *
     * @param event
     */
    void compensate(E event);

}
