package com.traveladvisor.hotelserver.service.datasource.hotel.mapper;

import com.traveladvisor.common.datasource.hotel.entity.HotelEntity;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.common.domain.vo.BookingId;
import com.traveladvisor.common.domain.vo.HotelId;
import com.traveladvisor.common.domain.vo.HotelOfferId;
import com.traveladvisor.hotelserver.service.datasource.hotel.entity.BookingApprovalEntity;
import com.traveladvisor.hotelserver.service.domain.entity.BookingApproval;
import com.traveladvisor.hotelserver.service.domain.entity.Hotel;
import org.springframework.stereotype.Component;

@Component
public class HotelDatasourceMapper {

    /**
     * HotelEntity -> Hotel
     *
     * @param hotelEntity
     * @return Hotel
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
     * @return HotelEntity
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

    /**
     * BookingApprovalEntity -> BookingApproval
     *
     * @param entity BookingApprovalEntity
     * @return BookingApproval
     */
    public BookingApproval toDomain(BookingApprovalEntity entity) {
        return BookingApproval.builder()
                .bookingApprovalId(new BookingApprovalId(entity.getId()))
                .bookingId(new BookingId(entity.getBookingId()))
                .hotelOffersId(new HotelOfferId(entity.getHotelOffersId()))
                .status(entity.getStatus())
                .build();
    }

    /**
     * BookingApproval -> BookingApprovalEntity
     *
     * @param domain BookingApproval
     * @return BookingApprovalEntity
     */
    public BookingApprovalEntity toEntity(BookingApproval domain) {
        return BookingApprovalEntity.builder()
                .id(domain.getId().getValue())
                .bookingId(domain.getBookingId().getValue())
                .hotelOffersId(domain.getHotelOffersId().getValue())
                .status(domain.getStatus())
                .build();
    }

}
