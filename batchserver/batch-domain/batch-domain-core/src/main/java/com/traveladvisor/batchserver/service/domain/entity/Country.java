package com.traveladvisor.batchserver.service.domain.entity;

import com.traveladvisor.common.domain.entity.AggregateRoot;
import com.traveladvisor.common.domain.vo.CountryId;

/**
 * Batch 도메인의 Aggregate Root 입니다. 도메인의 핵심 엔터티로, 다른 Entity와 Value Object를 포함합니다.
 * 외부 의존성을 완전히 배제시키기 위해 POJO 로 작성합니다.
 *
 * Entity는 Aggregate Root를 기준으로 불변성을 유지합니다. Aggregate Root는 Entity의 상태를 변경하는 메서드를 제공하며,
 * 이 메서드를 통해서만 Entity의 상태를 변경할 수 있습니다.
 *
 * Entity에는 setter를 잘 사용하지 않습니다. 본인은 의미를 알고 setter로 해당 필드의 상태를 변화시키려 하지만,
 * 제 3자는 그 의도를 알기 어렵기 때문입니다. 대신, 의도가 잘 드러나는 이름을 가진 메서드를 통해 상태를 변경하도록 합니다.
 */
public class Country extends AggregateRoot<CountryId> {

    private String name;
    private String nameTranslated;
    private String isoCode;
    private String isoCode2;
    private String iataCode;
    private Double longitude;
    private Double latitude;

    public Country(Builder builder) {
        super.setId(builder.countryId);
        this.name = builder.name;
        this.nameTranslated = builder.nameTranslated;
        this.isoCode = builder.isoCode;
        this.isoCode2 = builder.isoCode2;
        this.iataCode = builder.iataCode;
        this.longitude = builder.longitude;
        this.latitude = builder.latitude;
    }

    // BEGIN: Getter
    public String getName() {
        return name;
    }

    public String getNameTranslated() {
        return nameTranslated;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getIsoCode2() {
        return isoCode2;
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
    // END: Getter

    // BEGIN: Builder
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private CountryId countryId;
        private String name;
        private String nameTranslated;
        private String isoCode;
        private String isoCode2;
        private String iataCode;
        private double longitude;
        private double latitude;

        private Builder() {
        }

        public Builder id(CountryId countryId) {
            this.countryId = countryId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder nameTranslated(String nameTranslated) {
            this.nameTranslated = nameTranslated;
            return this;
        }

        public Builder isoCode(String isoCode) {
            this.isoCode = isoCode;
            return this;
        }

        public Builder isoCode2(String isoCode2) {
            this.isoCode2 = isoCode2;
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

        public Country build() {
            return new Country(this);
        }
    }
    // END: Builder

}
