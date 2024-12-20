package com.traveladvisor.memberserver.service.message.outbound.client;

import com.traveladvisor.common.domain.vo.EventActionType;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.UUID;

import static com.traveladvisor.common.domain.constant.common.TraceConstants.CORRELATION_ID;

@FeignClient(name="payment", url = "http://payment:9300"/*, fallback = PaymentServiceFeignFallback.class*/)
public interface PaymentServiceFeignClient {

    /**
     * 회원의 포인트 계좌를 생성하는 요청을 보냅니다.
     *
     * @param correlationId 요청 추적을 위한 상관관계 ID
     * @param memberId      회원 ID
     * @return 포인트 계좌 생성 결과 응답
     */
    @PostMapping(value = "/api/point-entries", consumes = "application/json")
    ResponseEntity<Map<String, Object>> createPointEntries(@RequestHeader(CORRELATION_ID) String correlationId,
                                                           @RequestParam UUID memberId);

    /**
     * 이벤트 종류에 따라 회원에게 보상을 지급하는 요청을 보냅니다.
     *
     * @param correlationId       요청 추적을 위한 상관관계 ID
     * @param memberId            회원 ID
     * @param eventActionType  이벤트 트리거 유형 (예: 회원가입, 추천인 추천, 리뷰 작성)
     * @return 리워드 지급 결과 응답
     */
    @PostMapping(value = "/api/rewards", consumes = "application/json")
    ResponseEntity<Map<String, Object>> rewardMember(@RequestHeader(CORRELATION_ID) String correlationId,
                                                     @RequestParam UUID memberId,
                                                     @RequestParam EventActionType eventActionType);

}
