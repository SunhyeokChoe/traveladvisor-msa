package com.traveladvisor.carserver.service.datasource.car.adapter;

import com.traveladvisor.carserver.service.datasource.car.mapper.CarDatasourceMapper;
import com.traveladvisor.carserver.service.datasource.car.repository.BookingApprovalJpaRepository;
import com.traveladvisor.carserver.service.domain.entity.BookingApproval;
import com.traveladvisor.carserver.service.domain.port.output.repository.BookingApprovalRepository;
import com.traveladvisor.common.domain.vo.BookingApprovalId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    @Override
    public Optional<BookingApproval> findById(BookingApprovalId bookingApprovalId) {
        return bookingApprovalJpaRepository.findById(bookingApprovalId.getValue())
                .map(carDatasourceMapper::toDomain);
    }

}
