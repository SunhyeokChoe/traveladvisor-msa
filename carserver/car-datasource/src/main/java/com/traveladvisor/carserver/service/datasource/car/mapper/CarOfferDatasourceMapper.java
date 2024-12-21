package com.traveladvisor.carserver.service.datasource.car.mapper;

import com.traveladvisor.carserver.service.domain.entity.CarOffer;
import com.traveladvisor.common.datasource.car.entity.CarOfferEntity;
import com.traveladvisor.common.domain.vo.CarOfferId;
import org.springframework.stereotype.Component;

@Component
public class CarOfferDatasourceMapper {

    /**
     * CarOfferEntity -> CarOffer
     *
     * @param carOfferEntity CarOfferEntity
     * @return CarOffer
     */
    public CarOffer toDomain(CarOfferEntity carOfferEntity) {
        return CarOffer.builder()
                .id(new CarOfferId(carOfferEntity.getId()))
                .vehicleCode(carOfferEntity.getVehicleCode())
                .category(carOfferEntity.getCategory())
                .description(carOfferEntity.getDescription())
                .imageUrl(carOfferEntity.getImageUrl())
                .baggages(carOfferEntity.getBaggages())
                .seats(carOfferEntity.getSeats())
                .price(carOfferEntity.getPrice())
                .build();
    }

    /**
     * CarOffer -> CarOfferEntity
     *
     * @param carOffer CarOffer
     * @return CarOfferEntity
     */
    public CarOfferEntity toEntity(CarOffer carOffer) {
        return CarOfferEntity.builder()
                .vehicleCode(carOffer.getVehicleCode())
                .category(carOffer.getCategory())
                .description(carOffer.getDescription())
                .imageUrl(carOffer.getImageUrl())
                .baggages(carOffer.getBaggages())
                .seats(carOffer.getSeats())
                .price(carOffer.getPrice())
                .build();
    }

}
