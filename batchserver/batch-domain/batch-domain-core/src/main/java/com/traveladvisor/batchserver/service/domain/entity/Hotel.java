package com.traveladvisor.batchserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.AggregateRoot;
import com.traveladvisor.common.domain.vo.HotelId;

import java.time.LocalDateTime;

/**
 * Batch 도메인의 Aggregate Root 입니다. 도메인의 핵심 엔터티로, 다른 Entity와 Value Object를 포함합니다.
 * 외부 의존성을 완전히 배제시키기 위해 POJO 로 작성합니다.
 *
 * Entity는 Aggregate Root를 기준으로 불변성을 유지합니다. Aggregate Root는 Entity의 상태를 변경하는 메서드를 제공하며,
 * 이 메서드를 통해서만 Entity의 상태를 변경할 수 있습니다.
 *
 * Entity에는 setter를 잘 사용하지 않습니다. 본인은 의미를 알고 setter로 해당 필드의 상태를 변화시키려 하지만,
 * 제 3자는 그 의도를 알기 어렵기 때문입니다. 대신, 의도가 잘 드러나는 이름을 가진 메서드를 통해 상태를 변경하도록 합니다.
 *
 * ※ hotel 마이크로서비스의 Hotel 도메인 클래스와 중복되는 것 처럼 보일 수 있으나, MSA + DDD 관점에서 도메인은
 * 각 마이크로서비스가 비즈니스 니즈에 따라 코어에 각기 따로 갖고 관리하며, 이는 다른 마이크로서비스의 도메인과는 완전히 별개입니다.
 * 다만, ValueObject는 공유할 수 있습니다. 예컨대 이 Batch 서비스의 HotelId는 Hotel 서비스의 Hotel 도메인의 HotelId와 동일합니다.
 */
public class Hotel extends AggregateRoot<HotelId> {

    private final String otaHotelId;
    private final String name;
    private final String iataCode;
    private final double longitude;
    private final double latitude;
    private final String chainCode;
    private final String countryCode;
    private final long dupeId;
    private final LocalDateTime otaLastUpdate;

    private Hotel(Builder builder) {
        super.setId(builder.hotelId);
        this.otaHotelId = builder.otaHotelId;
        this.name = builder.name;
        this.iataCode = builder.iataCode;
        this.longitude = builder.longitude;
        this.latitude = builder.latitude;
        this.chainCode = builder.chainCode;
        this.countryCode = builder.countryCode;
        this.dupeId = builder.dupeId;
        this.otaLastUpdate = builder.otaLastUpdate;
    }

    // BEGIN: Getter
    public String getOtaHotelId() {
        return otaHotelId;
    }

    public String getName() {
        return name;
    }

    public String getIataCode() {
        return iataCode;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getChainCode() {
        return chainCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public long getDupeId() {
        return dupeId;
    }

    public LocalDateTime getOtaLastUpdate() {
        return otaLastUpdate;
    }
    // END: Getter

    // BEGIN: Builder
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private HotelId hotelId;
        private String otaHotelId;
        private String name;
        private String iataCode;
        private double longitude;
        private double latitude;
        private String chainCode;
        private String countryCode;
        private long dupeId;
        private LocalDateTime otaLastUpdate;

        private Builder() {
        }

        public Builder id(HotelId hotelId) {
            this.hotelId = hotelId;
            return this;
        }

        public Builder otaHotelId(String otaHotelId) {
            this.otaHotelId = otaHotelId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder iataCode(String iataCode) {
            this.iataCode = iataCode;
            return this;
        }

        public Builder longitude(double longitude) {
            this.longitude = longitude;
            return this;
        }

        public Builder latitude(double latitude) {
            this.latitude = latitude;
            return this;
        }

        public Builder chainCode(String chainCode) {
            this.chainCode = chainCode;
            return this;
        }

        public Builder countryCode(String countryCode) {
            this.countryCode = countryCode;
            return this;
        }

        public Builder dupeId(long dupeId) {
            this.dupeId = dupeId;
            return this;
        }

        public Builder otaLastUpdate(LocalDateTime otaLastUpdate) {
            this.otaLastUpdate = otaLastUpdate;
            return this;
        }

        public Hotel build() {
            return new Hotel(this);
        }
    }
    // END: Builder
}