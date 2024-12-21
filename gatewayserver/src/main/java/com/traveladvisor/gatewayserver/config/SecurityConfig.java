package com.traveladvisor.gatewayserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.security.web.server.csrf.ServerCsrfTokenRequestHandler;
import org.springframework.security.web.server.csrf.XorServerCsrfTokenRequestAttributeHandler;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        // HTTP Only 설정을 해제해야 클라이언트 측에서 자바스크립트로 쿠키를 읽을 수 있습니다.
        // 반대로 true로 설정할 경우 쿠키를 읽을 수 없게 됩니다.
        // 그럼 React, Vue, Angular같은 SPA 환경의 퍼블릭 클라이언트는 CSRF 토큰을 응답 쿠키에서 읽을 수 없어 모든 CUD 요청에 대해 403 에러가 발생할 것입니다.
        CookieServerCsrfTokenRepository csrfTokenRepository = new CookieServerCsrfTokenRepository();
        csrfTokenRepository.setCookieCustomizer(cookieBuilder -> cookieBuilder.httpOnly(true));

        // CSRF BREACH 공격을 방어합니다.
        ServerCsrfTokenRequestHandler csrfTokenRequestHandler = new XorServerCsrfTokenRequestAttributeHandler();

        serverHttpSecurity.authorizeExchange(exchanges -> exchanges.pathMatchers(HttpMethod.GET).permitAll()
                        .pathMatchers("/batch/**").hasRole("DEVELOPER")
                        .pathMatchers("/member/**").hasAnyRole("TRAVELADVISOR_MEMBER", "TRAVELADVISOR_GATEWAY")
                        .pathMatchers("/payment/**").hasRole("TRAVELADVISOR_GATEWAY")
                        .pathMatchers("/booking/**").hasRole("TRAVELADVISOR_GATEWAY")
                        .pathMatchers("/hotel/**").hasRole("TRAVELADVISOR_GATEWAY")
                        .pathMatchers("/flight/**").hasRole("TRAVELADVISOR_GATEWAY")
                        .pathMatchers("/car/**").hasRole("TRAVELADVISOR_GATEWAY")
                        .pathMatchers("/contact-us/**").permitAll())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository)
                        .csrfTokenRequestHandler(csrfTokenRequestHandler)
                )
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(grantedAuthoritiesExtractor()))
                );

        return serverHttpSecurity.build();
    }

    /**
     * JWT 토큰에서 Role을 추출한 후 Collection<GrantedAuthority> 타입으로 반환하는 메서드를 Reactive JWT Auth 컨버터로 지정합니다.
     *
     * @return
     */
    private Converter<Jwt, Mono<AbstractAuthenticationToken>> grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KeyCloakJwtRoleConverter());

        return new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter);
    }

}
