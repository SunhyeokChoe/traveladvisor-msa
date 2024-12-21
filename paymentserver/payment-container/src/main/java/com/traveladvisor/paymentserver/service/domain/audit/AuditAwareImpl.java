package com.traveladvisor.paymentserver.service.domain.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    /**
     * 현재 리소스를 수정한 주체를 반환합니다.
     *
     * @return 수정자 이름
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("PAYMENT_MICROSERVICE");
    }
	
}
