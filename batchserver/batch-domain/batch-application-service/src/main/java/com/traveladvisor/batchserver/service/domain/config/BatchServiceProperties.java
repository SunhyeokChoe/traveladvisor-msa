package com.traveladvisor.batchserver.service.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "batch")
public class BatchServiceProperties {

    private Amadeus amadeus;

    @Data
    public static class Amadeus {
        private String baseUrl;
        private Endpoints endpoints;

        @Data
        public static class Endpoints {
            private Security security;
            private ReferenceData referenceData;

            @Data
            public static class Security {
                private String token;
            }

            @Data
            public static class ReferenceData {
                private Location location;

                @Data
                public static class Location {
                    private Hotels hotels;

                    @Data
                    public static class Hotels {
                        private String byCity;
                    }
                }
            }
        }
    }

}
