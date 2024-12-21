package com.traveladvisor.memberserver.service.message.outbound.adapter;

import com.traveladvisor.common.domain.vo.EventActionType;
import com.traveladvisor.memberserver.service.domain.port.output.client.PaymentServiceApiClient;
import com.traveladvisor.memberserver.service.message.outbound.client.PaymentServiceFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class PaymentServiceApiClientImpl implements PaymentServiceApiClient {

    private final PaymentServiceFeignClient paymentServiceFeignClient;

    @Override
    public ResponseEntity<Map<String, Object>> createPointEntries(String correlationId, UUID memberId) {
        return paymentServiceFeignClient.createPointEntries(correlationId, memberId);
    }

    @Override
    public ResponseEntity<Map<String, Object>> rewardMember(
            String correlationId, UUID memberId, EventActionType eventActionType) {
        return paymentServiceFeignClient.rewardMember(correlationId, memberId, eventActionType);
    }

}
