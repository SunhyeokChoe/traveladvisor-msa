package com.traveladvisor.common.domain.saga;

/**
 * Saga Action 진행 상태
 */
public enum SagaActionStatus {

    /**
     * Saga Action 시작
     */
    STARTED,
    
    /**
     * Saga Action 진행 중
     */
    PROCESSING,

    /**
     * Saga Action 보상 진행 중
     */
    COMPENSATING,

    /**
     * Saga Action 성공
     */
    SUCCEEDED,

    /**
     * Saga Action 보상 완료
     */
    COMPENSATED,

    /**
     * Saga Action 실패
     */
    FAILED,

}
