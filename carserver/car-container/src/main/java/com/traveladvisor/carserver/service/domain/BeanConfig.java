package com.traveladvisor.carserver.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    /**
     * domain-core 에 있는 도메인에는 스프링 관련 종속성을 일절 추가하지 않고, POJO로 구성되도록 했습니다. (POJO는 스프링 애너테이션으로 Bean 으로 만들 수 없습니다.)
     * DDD의 설계대로 이는 의도된 것이며, Spring Boot Context에서 Bean으로써 사용될 수 있도록 이 곳에서 빈으로 등록했습니다.
     * Bean으로 만들고 Car Application Service에서 이를 주입받아 사용합니다.
     */
    @Bean
    public CarDomainService CarDomainService() {
        return new CarDomainServiceImpl();
    }

}
