package com.traveladvisor.hotelserver.service.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "kafka.topic.booking")
public class HotelServiceConfig {

    private String inboundTopicName;
    private String outboundTopicName;

}
