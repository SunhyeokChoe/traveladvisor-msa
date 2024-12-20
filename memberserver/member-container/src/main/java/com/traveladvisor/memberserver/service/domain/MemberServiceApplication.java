package com.traveladvisor.memberserver.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableDiscoveryClient
// JPA 레포는 데이터 소스 모듈에 있습니다.
@EnableJpaRepositories(basePackages = { "com.traveladvisor.memberserver.service.datasource", "com.traveladvisor.common.datasource" })
// Entity 또한 데이터 소스 모듈에 있습니다.
@EntityScan(basePackages = { "com.traveladvisor.memberserver.service.datasource", "com.traveladvisor.common.datasource" })
@EnableFeignClients(basePackages = "com.traveladvisor.memberserver.service.message.outbound.client")
@SpringBootApplication(scanBasePackages = "com.traveladvisor.memberserver")
public class MemberServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberServiceApplication.class, args);
    }

}
