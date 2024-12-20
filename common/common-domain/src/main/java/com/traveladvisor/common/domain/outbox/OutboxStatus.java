package com.traveladvisor.common.domain.outbox;

/**
 * Outbox 상태를 나타냅니다. 이벤트의 로컬 트랜잭션 진행 상태를 나타내기 위해 사용됩니다.
 */
public enum OutboxStatus {

    /**
     * 이벤트가 발행됨
     */
    STARTED,

    /**
     * 이벤트 정상 처리 완료
     */
    COMPLETED,

    /**
     * 이벤트 처리 실패
     */
    FAILED

}
