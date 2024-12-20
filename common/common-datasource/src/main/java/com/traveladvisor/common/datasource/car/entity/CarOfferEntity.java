package com.traveladvisor.common.datasource.car.entity;

import com.traveladvisor.common.datasource.common.entity.BaseZonedDateTimeEntity;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "car_offers", schema = "car")
@Entity
public class CarOfferEntity extends BaseZonedDateTimeEntity {

    @Id @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_code")
    private String vehicleCode;

    @Column(name = "category")
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "baggages")
    private String baggages;

    @Column(name = "seats")
    private String seats;

    @Column(name = "price")
    private BigDecimal price;

}
