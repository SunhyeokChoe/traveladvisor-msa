package com.traveladvisor.paymentserver.service.domain.port.output.repository;

import com.traveladvisor.common.domain.vo.MemberId;
import com.traveladvisor.paymentserver.service.domain.entity.PointEntry;

import java.util.Optional;

public interface PointEntryRepository {

    Optional<PointEntry> findById(MemberId memberId);

    PointEntry save(PointEntry pointEntry);

}
