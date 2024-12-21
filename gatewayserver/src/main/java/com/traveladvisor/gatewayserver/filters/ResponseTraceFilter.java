package com.traveladvisor.gatewayserver.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import static com.traveladvisor.common.domain.constant.common.TraceConstants.CORRELATION_ID;

@Configuration
public class ResponseTraceFilter {

    private static final Logger logger = LoggerFactory.getLogger(ResponseTraceFilter.class);

    @Autowired
    CorrelationUtil correlationUtil;

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> {
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
                String correlationId = correlationUtil.getCorrelationId(requestHeaders);

                if(!(exchange.getResponse().getHeaders().containsKey(CORRELATION_ID))) {
                    logger.debug("Correlation ID를 out-bound 헤더에 업데이트했습니다. Correlation ID: {}", correlationId);
                    exchange.getResponse().getHeaders().add(CORRELATION_ID, correlationId);
                }

            }));
        };
    }

}
