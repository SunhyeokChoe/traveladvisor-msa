package com.traveladvisor.batchserver.service.datasource.hotel.mapper;

import com.traveladvisor.batchserver.service.domain.entity.Hotel;
import com.traveladvisor.common.datasource.hotel.entity.HotelEntity;
import com.traveladvisor.common.domain.vo.HotelId;
import org.springframework.stereotype.Component;

@Component
public class HotelDatasourceMapper {

    /**
     * HotelEntity -> Hotel
     *
     * @param hotelEntity
     * @return
     */
    public Hotel toDomain(HotelEntity hotelEntity) {
        return Hotel.builder()
                .id(new HotelId(hotelEntity.getId()))
                .otaHotelId(hotelEntity.getOtaHotelId())
                .name(hotelEntity.getName())
                .iataCode(hotelEntity.getIataCode())
                .longitude(hotelEntity.getLongitude())
                .latitude(hotelEntity.getLatitude())
                .chainCode(hotelEntity.getChainCode())
                .countryCode(hotelEntity.getCountryCode())
                .dupeId(hotelEntity.getDupeId())
                .otaLastUpdate(hotelEntity.getOtaLastUpdate())
                .build();
    }

    /**
     * Hotel -> HotelEntity
     *
     * @param hotel
     * @return
     */
    public HotelEntity toEntity(Hotel hotel) {
        return HotelEntity.builder()
                .otaHotelId(hotel.getOtaHotelId())
                .name(hotel.getName())
                .iataCode(hotel.getIataCode())
                .longitude(hotel.getLongitude())
                .latitude(hotel.getLatitude())
                .chainCode(hotel.getChainCode())
                .countryCode(hotel.getCountryCode())
                .dupeId(hotel.getDupeId())
                .otaLastUpdate(hotel.getOtaLastUpdate())
                .build();
    }

}
