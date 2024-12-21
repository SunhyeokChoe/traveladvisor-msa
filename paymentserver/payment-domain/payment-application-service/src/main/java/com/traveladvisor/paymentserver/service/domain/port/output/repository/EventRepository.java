package com.traveladvisor.paymentserver.service.domain.port.output.repository;

import com.traveladvisor.common.domain.vo.EventActionType;
import com.traveladvisor.paymentserver.service.domain.entity.Event;

import java.util.List;

public interface EventRepository {

    List<Event> findByActionType(EventActionType eventActionType);

}
