package com.traveladvisor.common.datasource.hotel.entity;

import com.traveladvisor.common.datasource.common.entity.BaseZonedDateTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "hotels", schema = "hotel")
@Entity
public class HotelEntity extends BaseZonedDateTimeEntity {

    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ota_hotel_id")
    private String otaHotelId;

    @Column(name = "name")
    private String name;

    @Column(name = "iata_code")
    private String iataCode;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "chain_code")
    private String chainCode;

    @Column(name = "country_code")
    private String countryCode;

    @Column(name = "dupe_id")
    private Long dupeId;

    @Column(name = "ota_last_update")
    private LocalDateTime otaLastUpdate;

}
