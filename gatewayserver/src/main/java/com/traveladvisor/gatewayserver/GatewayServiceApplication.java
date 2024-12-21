package com.traveladvisor.gatewayserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Spring Cloud Gateway 서비스의 메인 애플리케이션 클래스입니다.
 * <p>
 * 서버 사이드 디스커버리, 로드밸런서를 사용합니다. 이러한 책임을 다음과 같이 K8s 요소에 위임합니다.
 * K8s Discovery Server: 마이크로서비스 탐지 및 관리
 * K8s Service         : 마이크로서비스로의 트래픽 로드밸런싱
 * <p>
 * 각 마이크로서비스는 DNS 이름을 통해 K8s 디스커버리 서버에 의해 쿠버네티스 클러스터 내에서 발견될 수 있습니다.
 * 따라서 게이트웨이 서비스는 클러스터 내 각 마이크로서비스의 DNS 이름을 명시하기만 하면 K8s 디스커버리 서버와 K8s 서비스가
 * 서로 협력해 마이크로서비스를 발견하고 요청을 포워딩 해줍니다.
 */
@SpringBootApplication
@EnableDiscoveryClient // K8s Discovery Server로부터 탐지되기 위한 디스커버리 클라이언트 설정입니다.
public class GatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServiceApplication.class, args);
    }

    @Bean
    public RouteLocator routeConfig(RouteLocatorBuilder routeLocatorBuilder) {
        return routeLocatorBuilder.routes()
                .route(p -> p
                        .path("/batch/**")
                        .filters(f -> f.rewritePath("/batch/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("batchServiceCircuitBreaker")
                                        .setFallbackUri("forward:/contact-us"))
                                .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(userKeyResolver())))
                        .uri("http://batchserver:9100")) // 이 프로젝트는 K8s Discovery Server를 사용하므로 쿠버네티스 클러스터 내 서비스 호출을 위해 DNS 이름을 사용합니다.
                .route(p -> p
                        .path("/member/**")
                        .filters(f -> f.rewritePath("/member/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("memberCircuitBreaker")
                                        .setFallbackUri("forward:/contact-us"))
                                .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(userKeyResolver()))
                                .retry(retryConfig -> retryConfig.setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)))
                        .uri("http://memberserver:9200"))
                .route(p -> p
                        .path("/payment/**")
                        .filters(f -> f.rewritePath("/payment/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("paymentCircuitBreaker")
                                        .setFallbackUri("forward:/contact-us"))
                                .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(userKeyResolver()))
                                .retry(retryConfig -> retryConfig.setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)))
                        .uri("http://paymentserver:9300"))
                .route(p -> p
                        .path("/booking/**")
                        .filters(f -> f.rewritePath("/booking/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("bookingCircuitBreaker")
                                        .setFallbackUri("forward:/contact-us"))
                                .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(userKeyResolver()))
                                .retry(retryConfig -> retryConfig.setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)))
                        .uri("http://bookingserver:9400"))
                .route(p -> p
                        .path("/hotel/**")
                        .filters(f -> f.rewritePath("/hotel/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("hotelCircuitBreaker")
                                        .setFallbackUri("forward:/contact-us"))
                                .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(userKeyResolver()))
                                .retry(retryConfig -> retryConfig.setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)))
                        .uri("http://hotelserver:9500"))
                .route(p -> p
                        .path("/flight/**")
                        .filters(f -> f.rewritePath("/flight/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("flightCircuitBreaker")
                                        .setFallbackUri("forward:/contact-us"))
                                .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(userKeyResolver()))
                                .retry(retryConfig -> retryConfig.setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)))
                        .uri("http://flightserver:9600"))
                .route(p -> p
                        .path("/car/**")
                        .filters(f -> f.rewritePath("/car/(?<segment>.*)", "/${segment}")
                                .addResponseHeader("X-Response-Time", LocalDateTime.now().toString())
                                .circuitBreaker(config -> config.setName("carCircuitBreaker")
                                        .setFallbackUri("forward:/contact-us"))
                                .requestRateLimiter(config -> config.setRateLimiter(redisRateLimiter())
                                        .setKeyResolver(userKeyResolver()))
                                .retry(retryConfig -> retryConfig.setRetries(3)
                                        .setMethods(HttpMethod.GET)
                                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)))
                        .uri("http://carserver:9700"))
                .build();
    }

    @Bean
    public Customizer<ReactiveResilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(10))
                        .build()).build());
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(
                1,
                1,
                1
        );
    }

    @Bean
    KeyResolver userKeyResolver() {
        return exchange -> Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
                .defaultIfEmpty("anonymous");
    }

}
