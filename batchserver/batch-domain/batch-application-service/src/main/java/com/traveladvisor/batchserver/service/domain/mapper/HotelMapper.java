package com.traveladvisor.batchserver.service.domain.mapper;

import com.google.common.base.Optional;
import com.traveladvisor.batchserver.service.domain.entity.Hotel;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class HotelMapper {

    /**
     * Amadeus reference-data/locations/hotels JSON -> Hotel
     *
     * @param hotelData
     * @return
     */
    public Hotel toDomain(LinkedHashMap<String, Object> hotelData) {
        String chainCode = Optional.fromNullable(hotelData.get("chainCode"))
                .transform(Object::toString)
                .or("");

        String iataCode = Optional.fromNullable(hotelData.get("iataCode"))
                .transform(Object::toString)
                .or("");

        long dupeId = Optional.fromNullable(hotelData.get("dupeId"))
                .transform(value -> value instanceof Integer ? ((Integer) value).longValue() : (Long) value)
                .or(0L);

        String name = Optional.fromNullable(hotelData.get("name"))
                .transform(Object::toString)
                .or("");

        String otaHotelId = Optional.fromNullable(hotelData.get("hotelId"))
                .transform(Object::toString)
                .or("");

        String lastUpdateStr = Optional.fromNullable(hotelData.get("lastUpdate"))
                .transform(Object::toString)
                .or(LocalDateTime.now().toString());
        LocalDateTime lastUpdate = LocalDateTime.parse(lastUpdateStr, DateTimeFormatter.ISO_DATE_TIME);

        /**
         * geoCode 데이터는 다음과 같습니다. latitude, longitude를 추출합니다.
         *
         * "geoCode": {
         *   "latitude": 37.56251,
         *   "longitude": 126.96951
         * },
         */
        Map<String, Object> geoCode = Optional.fromNullable((Map<String, Object>) hotelData.get("geoCode"))
                .or(new LinkedHashMap<>());
        double latitude = Optional.fromNullable((Double) geoCode.get("latitude")).or(0.0);
        double longitude = Optional.fromNullable((Double) geoCode.get("longitude")).or(0.0);

        /**
         * address 데이터는 다음과 같습니다. countryCode를 추출합니다.
         *
         * "address": {
         *   "countryCode": "KR"
         * }
         */
        Map<String, Object> address = Optional.fromNullable((Map<String, Object>) hotelData.get("address"))
                .or(new LinkedHashMap<>());

        String countryCode = Optional.fromNullable(address.get("countryCode"))
                .transform(Object::toString)
                .or("");

        return Hotel.builder()
                .otaHotelId(otaHotelId)
                .name(name)
                .iataCode(iataCode)
                .longitude(longitude)
                .latitude(latitude)
                .chainCode(chainCode)
                .countryCode(countryCode)
                .dupeId(dupeId)
                .otaLastUpdate(lastUpdate)
                .build();
    }

}
