package com.traveladvisor.hotelserver.service.domain.port.output.repository;

import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.domain.vo.HotelBookingApprovalStatus;
import com.traveladvisor.hotelserver.service.domain.outbox.model.BookingOutbox;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingOutboxRepository {

    /**
     * booking_outbox 메시지함에 호텔 예약 처리 결과를 저장합니다.
     *
     * @param orderOutboxMessage
     * @return
     */
    BookingOutbox save(BookingOutbox orderOutboxMessage);

    /**
     * booking_outbox 메시지함에서 BookingOutbox 목록을 조회합니다.
     *
     * @param eventType
     * @param status
     * @return
     */
    Optional<List<BookingOutbox>> findByTypeAndOutboxStatus(String eventType, OutboxStatus status);

    /**
     * booking_outbox 메시지함에서 BookingOutbox를 조회합니다.
     *
     * @param eventType
     * @param sagaActionId
     * @param hotelBookingApprovalStatus
     * @param outboxStatus
     * @return
     */
    Optional<BookingOutbox> findByEventTypeAndSagaActionIdAndBookingStatusAndOutboxStatus(String eventType,
                                                                                          UUID sagaActionId,
                                                                                          HotelBookingApprovalStatus hotelBookingApprovalStatus,
                                                                                          OutboxStatus outboxStatus);

    /**
     * booking_outbox 메시지함에서 처리된 메시지를 삭제합니다.
     *
     * @param eventType
     * @param outboxStatus
     */
    void deleteByTypeAndOutboxStatus(String eventType, OutboxStatus outboxStatus);

}
