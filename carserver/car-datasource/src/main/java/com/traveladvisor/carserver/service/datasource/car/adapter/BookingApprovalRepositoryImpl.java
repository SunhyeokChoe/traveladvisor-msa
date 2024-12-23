package com.traveladvisor.carserver.service.datasource.car.adapter;

import com.traveladvisor.carserver.service.datasource.car.mapper.CarDatasourceMapper;
import com.traveladvisor.carserver.service.datasource.car.repository.BookingApprovalJpaRepository;
import com.traveladvisor.carserver.service.domain.entity.BookingApproval;
import com.traveladvisor.carserver.service.domain.port.output.repository.BookingApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookingApprovalRepositoryImpl implements BookingApprovalRepository {

    private final BookingApprovalJpaRepository bookingApprovalJpaRepository;
    private final CarDatasourceMapper carDatasourceMapper;

    @Override
    public BookingApproval save(BookingApproval bookingApproval) {
        return carDatasourceMapper.toDomain(
                bookingApprovalJpaRepository.save(carDatasourceMapper.toEntity(bookingApproval)));
    }

}
