package com.traveladvisor.batchserver.service.datasource.hotel.mapper;

import com.traveladvisor.batchserver.service.domain.entity.Country;
import com.traveladvisor.common.datasource.hotel.entity.CountryEntity;
import com.traveladvisor.common.domain.vo.CountryId;
import org.springframework.stereotype.Component;

@Component
public class CountryDatasourceMapper {

    /**
     * CountryEntity -> Country
     *
     * @param countryEntity
     * @return
     */
    public Country toDomain(CountryEntity countryEntity) {
        return Country.builder()
                .id(new CountryId(countryEntity.getId()))
                .name(countryEntity.getName())
                .nameTranslated(countryEntity.getNameTranslated())
                .isoCode(countryEntity.getIsoCode())
                .isoCode2(countryEntity.getIsoCode2())
                .iataCode(countryEntity.getIataCode())
                .longitude(countryEntity.getLongitude())
                .latitude(countryEntity.getLatitude())
                .build();
    }

}
