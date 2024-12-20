package com.traveladvisor.common.datasource.hotel.entity;

import com.traveladvisor.common.datasource.common.entity.BaseZonedDateTimeEntity;
import lombok.*;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "countries", schema = "hotel")
@Entity
public class CountryEntity extends BaseZonedDateTimeEntity {

    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "name_translated")
    private String nameTranslated;

    @Column(name = "iso_code")
    private String isoCode;

    @Column(name = "iso_code2")
    private String isoCode2;

    @Column(name = "iata_code")
    private String iataCode;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

}
