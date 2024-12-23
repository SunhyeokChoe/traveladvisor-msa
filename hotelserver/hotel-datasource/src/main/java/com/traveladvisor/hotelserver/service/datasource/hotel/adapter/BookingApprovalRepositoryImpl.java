package com.traveladvisor.hotelserver.service.datasource.hotel.adapter;

import com.traveladvisor.common.domain.vo.BookingApprovalId;
import com.traveladvisor.hotelserver.service.datasource.hotel.mapper.HotelDatasourceMapper;
import com.traveladvisor.hotelserver.service.datasource.hotel.repository.BookingApprovalJpaRepository;
import com.traveladvisor.hotelserver.service.domain.entity.BookingApproval;
import com.traveladvisor.hotelserver.service.domain.port.output.repository.BookingApprovalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class BookingApprovalRepositoryImpl implements BookingApprovalRepository {

    private final BookingApprovalJpaRepository bookingApprovalJpaRepository;
    private final HotelDatasourceMapper hotelDatasourceMapper;

    @Override
    public BookingApproval save(BookingApproval bookingApproval) {
        return hotelDatasourceMapper.toDomain(
                bookingApprovalJpaRepository.save(hotelDatasourceMapper.toEntity(bookingApproval)));
    }

    @Override
    public Optional<BookingApproval> findById(BookingApprovalId bookingApprovalId) {
        return bookingApprovalJpaRepository.findById(bookingApprovalId.getValue())
                .map(hotelDatasourceMapper::toDomain);
    }

}
