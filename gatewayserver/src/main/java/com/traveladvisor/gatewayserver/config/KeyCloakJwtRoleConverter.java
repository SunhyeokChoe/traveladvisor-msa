package com.traveladvisor.gatewayserver.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * KeyCloak이 액세스 토큰을 생성할 때 토큰 내부에 엔드유저 혹은 클라이언트 애플리케이션에 대한 다양한 정보가 포함되는데,
 * 이러한 정보에는 사용자 이름, 사용자 이메일, 사용자의 역할, 클라이언트 애플리케이션의 이메일, 클라이언트 애플리케이션의 역할 등이 있습니다.
 *
 * 이 클래스는 KeyCloak으로 전달받은 액세스 토큰에서 역할 정보를 추출한 뒤 SimpleGrantedAuthority 형태로 변환하여 반환하는 책임을 담당합니다.
 * 변환하는 이유는 스프링 시큐리티 프레임워크는 역할 정보가 GrantedAuthority 혹은 SimpleGrantedAuthority 객체 타입으로 구성되어야
 * Role 또는 Authority 정보로 인지할 수 있기 때문입니다.
 *
 * 이를 위해 Converter 인터페이스를 구현합니다. 인자로 {@link Jwt}를
 * 입력으로 받아 Collection<GrantedAuthority>로 변환해 반환합니다.
 */
public class KeyCloakJwtRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
         /*
            realm_access 정보를 추출합니다. 데이터 예시

            "realm_access": {
                "roles": [
                  "offline_access",
                  "uma_authorization",
                  "ADMIN",
                  "USER",
                  "default-roles-traveladvisor-prod"
                ]
            },
         */
        Map<String, Object> realmAccess = (Map<String, Object>) source.getClaims().get("realm_access");
        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }

        // 1. 역할 정보(roles) 문자열을 추출합니다.
        // 2. 시큐리티가 이해할 수 있도록 접두에 ROLE_ 연결합니다.
        // 3. 시큐리티가 Role로써 이해할 수 있도록 각 역할 정보 문자열을 SimpleGrantedAuthority 객체로 변환합니다.
        // 4. 마지막으로 컬렉션으로 변경 후 반환합니다.
        Collection<GrantedAuthority> returnValue = ((List<String>) realmAccess.get("roles"))
                .stream().map(roleName -> "ROLE_" + roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return returnValue;
    }

}
