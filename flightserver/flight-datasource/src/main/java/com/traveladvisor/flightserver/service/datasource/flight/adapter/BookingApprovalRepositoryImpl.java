package com.traveladvisor.flightserver.service.datasource.flight.adapter;

import com.traveladvisor.flightserver.service.datasource.flight.mapper.FlightDatasourceMapper;
import com.traveladvisor.flightserver.service.datasource.flight.repository.BookingApprovalJpaRepository;
import com.traveladvisor.flightserver.service.domain.entity.BookingApproval;
import com.traveladvisor.flightserver.service.domain.port.output.repository.BookingApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookingApprovalRepositoryImpl implements BookingApprovalRepository {

    private final BookingApprovalJpaRepository bookingApprovalJpaRepository;
    private final FlightDatasourceMapper flightDatasourceMapper;

    @Override
    public BookingApproval save(BookingApproval bookingApproval) {
        return flightDatasourceMapper.toDomain(
                bookingApprovalJpaRepository.save(flightDatasourceMapper.toEntity(bookingApproval)));
    }

}
