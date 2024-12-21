package com.traveladvisor.paymentserver.service.datasource.event.repository;

import com.traveladvisor.common.domain.vo.EventActionType;
import com.traveladvisor.paymentserver.service.datasource.event.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {

    List<EventEntity> findAllByActionType(EventActionType actionType);

}
