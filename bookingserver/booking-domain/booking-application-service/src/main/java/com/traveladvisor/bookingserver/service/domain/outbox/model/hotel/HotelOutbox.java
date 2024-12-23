package com.traveladvisor.bookingserver.service.domain.outbox.model.hotel;

import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.saga.SagaActionStatus;
import com.traveladvisor.common.domain.vo.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Booking 서비스에서 Hotel 서비스로 메시지를 보내기 위한 outbox 테이블입니다.
 * booking 스키마를 자세히 보시면 outbox 테이블이 하나만 있는 것이 아닌 Hotel, Flight, Car 에 해당하는 outbox 테이블이 총 세 개 있는 것을 볼 수 있습니다.
 * 각 이벤트는 서로 다른 비즈니스 니즈에 의해 생성되고, 이벤트를 처리하는 서비스 또한 각각 다릅니다. 따라서 하나의 outbox 테이블에서 모든 이벤트를 다루기 보다는
 * 각 비즈니스에 맞는 outbox 테이블을 별도로 만들어 그곳에서 관리하도록 합니다.
 */
@Getter @Builder
@AllArgsConstructor
public class HotelOutbox {

    private UUID id;
    private UUID sagaActionId;
    private String eventType;
    private String eventPayload;
    @Setter
    private BookingStatus bookingStatus;
    @Setter
    private OutboxStatus outboxStatus;
    @Setter
    private SagaActionStatus sagaActionStatus;
    @Setter
    private ZonedDateTime completedAt;
    private ZonedDateTime createdAt;
    private int version;

}
