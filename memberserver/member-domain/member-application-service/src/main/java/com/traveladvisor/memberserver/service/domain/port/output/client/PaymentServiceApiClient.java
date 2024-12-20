package com.traveladvisor.memberserver.service.domain.port.output.client;

import com.traveladvisor.common.domain.vo.EventActionType;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.UUID;

public interface PaymentServiceApiClient {

    /**
     * 회원의 포인트 계좌를 생성하는 요청을 보냅니다.
     *
     * @param memberId   회원 ID
     * @return 포인트 계좌 생성 결과 응답
     */
    ResponseEntity<Map<String, Object>> createPointEntries(String correlationId, UUID memberId);

    /**
     * 이벤트 종류에 따라 회원에게 보상을 지급하는 요청을 보냅니다.
     *
     * @param memberId    회원 ID
     * @param actionType  이벤트 트리거 유형 (예: 회원가입, 추천인 추천, 리뷰 작성)
     * @return 리워드 지급 결과 응답
     */
    ResponseEntity<Map<String, Object>> rewardMember(String correlationId, UUID memberId,
                                                     EventActionType actionType);

}
