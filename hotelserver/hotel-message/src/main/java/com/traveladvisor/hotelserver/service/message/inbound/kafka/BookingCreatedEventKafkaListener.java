package com.traveladvisor.hotelserver.service.message.inbound.kafka;

import com.traveladvisor.common.domain.event.booking.BookingCreatedEventPayload;
import com.traveladvisor.common.domain.vo.HotelBookingStatus;
import com.traveladvisor.common.kafka.consumer.KafkaSingleMessageConsumer;
import com.traveladvisor.common.kafka.producer.KafkaMessageHelper;
import com.traveladvisor.common.message.constant.DebeziumOperator;
import com.traveladvisor.hotelserver.service.domain.exception.HotelApplicationServiceException;
import com.traveladvisor.hotelserver.service.domain.exception.HotelNotFoundException;
import com.traveladvisor.hotelserver.service.domain.port.input.event.BookingCreatedEventListener;
import com.traveladvisor.hotelserver.service.message.mapper.HotelMessageMapper;
import debezium.booking.hotel_outbox.Envelope;
import debezium.booking.hotel_outbox.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
@Component
public class BookingCreatedEventKafkaListener implements KafkaSingleMessageConsumer<Envelope> {

    private final BookingCreatedEventListener bookingCreatedEventListener;
    private final HotelMessageMapper hotelMessageMapper;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    @KafkaListener(
            id = "${kafka.config.consumer.hotel-consumer-group-id}",
            topics = "${kafka.topic.booking.inbound-topic-name}")
    public void receive(@Payload Envelope message,
                        @Header(KafkaHeaders.RECEIVED_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) Integer partition,
                        @Header(KafkaHeaders.OFFSET) Long offset) {
        // Outbox WAL의 관찰 대상은 생성 트랜잭션이므로 CREATE OPERATOR 인 경우에만 처리할 수 있도록 합니다.
        if (message.getBefore() != null && !(DebeziumOperator.CREATE.getValue().equals(message.getOp()))) {
            return;
        }

        log.info("[IN-BOUND] 수신 메시지: {}, 키: {}, 파티션: {}, 오프셋: {}", message, key, partition, offset);

        // 카프카 메시지로부터 Avro 메시지와 EventPayload를 추출합니다.
        Value bookingCreatedEventAvroModel = message.getAfter();
        BookingCreatedEventPayload bookingCreatedEventPayload = kafkaMessageHelper.getEventPayload(
                bookingCreatedEventAvroModel.getEventPayload(), BookingCreatedEventPayload.class);

        try {
            switch (HotelBookingStatus.valueOf(bookingCreatedEventPayload.getHotelBookingStatus())) {
                // Booking 상태가 PENDING 인 경우 호텔 예약을 처리합니다.
                case PENDING -> {
                    bookingCreatedEventListener.processHotelBooking(hotelMessageMapper
                            .toHotelBookingCommand(bookingCreatedEventPayload, bookingCreatedEventAvroModel));
                }
                // Booking 상태가 CANCELLED 인 경우 호텔 예약을 취소 처리합니다.
                case CANCELLED -> {
                    bookingCreatedEventListener.compensateHotelBooking(hotelMessageMapper
                            .toHotelBookingCommand(bookingCreatedEventPayload, bookingCreatedEventAvroModel));
                }
                default -> log.warn("알 수 없는 호텔 예약 상태입니다. BookingId: {}", bookingCreatedEventPayload.getBookingId());
            }
        } catch (DataAccessException ex) {
            SQLException sqlException = (SQLException) ex.getRootCause();

            if (sqlException != null && sqlException.getSQLState() != null &&
                    PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
                // 유니크 인덱스 제약 조건 위반 예외 발생의 경우 이미 다른 Consumer에서 처리 한 요청이기에 로그만 남깁니다.
                log.error("[PSQLState: {}] 유니크 제약 조건 예외가 발생했습니다. 이미 처리된 예약서 ID: {}",
                        sqlException.getSQLState(), bookingCreatedEventPayload.getBookingId());
            } else {
                // 그 외의 데이터베이스 예외는 밖으로 전파시킵니다. 이는 @KafkaListener가 자동으로 재시도를 수행하게 합니다.
                throw new HotelApplicationServiceException("DataAccessException 예외가 발생했습니다. 예외 메시지: "
                        + ex.getMessage(), ex);
            }
        } catch (HotelNotFoundException ex) {
            // 이 @KafkaListener는 예약서 정보가 없으면 메시지 처리를 다시 시도하지 않도록 합니다.
            log.error("예약서를 찾지 못했습니다. BookingId: {}", bookingCreatedEventPayload.getBookingId());
        }

    }

}
