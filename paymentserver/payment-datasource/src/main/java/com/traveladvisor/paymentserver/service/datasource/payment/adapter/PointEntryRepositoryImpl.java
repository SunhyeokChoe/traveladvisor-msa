package com.traveladvisor.paymentserver.service.datasource.payment.adapter;

import com.traveladvisor.common.domain.vo.MemberId;
import com.traveladvisor.paymentserver.service.datasource.payment.mapper.PointEntryDatasourceMapper;
import com.traveladvisor.paymentserver.service.datasource.payment.repository.PointEntryJpaRepository;
import com.traveladvisor.paymentserver.service.domain.entity.PointEntry;
import com.traveladvisor.paymentserver.service.domain.port.output.repository.PointEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class PointEntryRepositoryImpl implements PointEntryRepository {

    private final PointEntryJpaRepository pointEntryJpaRepository;
    private final PointEntryDatasourceMapper pointEntryDatasourceMapper;

    @Override
    public Optional<PointEntry> findById(MemberId memberId) {
        return pointEntryJpaRepository.findById(memberId.getValue())
                .map(pointEntryDatasourceMapper::toDomain);
    }

    @Override
    public PointEntry save(PointEntry pointEntry) {
        return pointEntryDatasourceMapper.toDomain(
                pointEntryJpaRepository.save(pointEntryDatasourceMapper.toEntity(pointEntry)));
    }

}
