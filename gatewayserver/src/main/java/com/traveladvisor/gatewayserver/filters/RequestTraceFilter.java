package com.traveladvisor.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.traveladvisor.common.domain.constant.common.TraceConstants.CORRELATION_ID;

@Order(1)
@Component
public class RequestTraceFilter implements GlobalFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestTraceFilter.class);

    @Autowired
    CorrelationUtil correlationUtil;

    /**
     * 요청 헤더에서 Correlation ID를 가져오거나 없는 경우 새로 생성해 요청 헤더에 추가합니다.
     *
     * @param exchange 요청 객체
     * @param chain    필터 체인
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
        String correlationID = correlationUtil.getCorrelationId(requestHeaders);

        if (correlationID == null) {
            correlationID = correlationUtil.generateCorrelationId();
            exchange = correlationUtil.setCorrelationId(exchange, correlationID);
            logger.debug("요청 헤더에서 Correlation ID를 찾지 못해 새로 생성합니다. 생성된 헤더 -> {}: {}",
                    CORRELATION_ID, correlationID);
        }

        return chain.filter(exchange);
    }

}
