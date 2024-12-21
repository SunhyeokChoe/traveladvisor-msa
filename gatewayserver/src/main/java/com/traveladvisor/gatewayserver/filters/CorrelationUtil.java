package com.traveladvisor.gatewayserver.filters;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Optional;

import static com.traveladvisor.common.domain.constant.common.TraceConstants.CORRELATION_ID;

/**
 * 요청 헤더에서 Correlation ID를 추출 및 추가하는 유틸리티 클래스입니다.
 *
 * 마이크로서비스 환경에서는 하나의 요청이 여러 서비스에 걸쳐 처리될 수 있습니다.
 * 요청 간 연관성을 추적하기 위한 수단으로 Correlation ID를 사용합니다.
 * Correlation ID를 사용하면 로그를 분석할 때 요청의 흐름을 쉽게 파악할 수 있습니다.
 */
@Component
public class CorrelationUtil {

    public String getCorrelationId(HttpHeaders requestHeaders) {
        return Optional.ofNullable(requestHeaders.get(CORRELATION_ID))
                .flatMap(list -> list.stream().findFirst())
                .orElse(null);
    }

    /**
     * 기존 요청 헤더에 새로운 헤더를 추가하여 반환합니다.
     *
     * @param exchange  요청 객체
     * @param name      추가할 헤더 이름
     * @param value     추가할 헤더 값
     * @return          새로운 헤더가 추가된 요청 객체
     */
    public ServerWebExchange setRequestHeader(ServerWebExchange exchange, String name, String value) {
        return exchange.mutate().request(exchange.getRequest().mutate().header(name, value).build()).build();
    }

    /**
     * 요청 객체에 Correlation ID를 추가하여 반환합니다.
     *
     * @param exchange      요청 객체
     * @param correlationId Correlation ID
     * @return              Correlation ID가 추가된 요청 객체
     */
    public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
        return this.setRequestHeader(exchange, CORRELATION_ID, correlationId);
    }

    /**
     * Correlation ID를 생성하여 반환합니다.
     *
     * @return 생성된 Correlation ID
     */
    public String generateCorrelationId() {
        return java.util.UUID.randomUUID().toString();
    }

}
