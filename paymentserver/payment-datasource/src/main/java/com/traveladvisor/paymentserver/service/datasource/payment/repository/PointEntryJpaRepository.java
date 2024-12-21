package com.traveladvisor.paymentserver.service.datasource.payment.repository;

import com.traveladvisor.paymentserver.service.datasource.payment.entity.PointEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PointEntryJpaRepository extends JpaRepository<PointEntryEntity, UUID> {

}
