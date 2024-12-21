package com.traveladvisor.paymentserver.service.datasource.event.adapter;

import com.traveladvisor.common.domain.vo.EventActionType;
import com.traveladvisor.paymentserver.service.datasource.event.mapper.EventDatasourceMapper;
import com.traveladvisor.paymentserver.service.datasource.event.repository.EventJpaRepository;
import com.traveladvisor.paymentserver.service.domain.entity.Event;
import com.traveladvisor.paymentserver.service.domain.port.output.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class EventRepositoryImpl implements EventRepository {

    private final EventJpaRepository eventJpaRepository;
    private final EventDatasourceMapper eventDatasourceMapper;

    @Override
    public List<Event> findByActionType(EventActionType eventActionType) {
        return eventJpaRepository.findAllByActionType(eventActionType)
                .stream()
                .map(eventDatasourceMapper::toDomain)
                .toList();
    }

}
