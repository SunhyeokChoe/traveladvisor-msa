package com.traveladvisor.carserver.service.message.inbound.kafka;

import com.traveladvisor.carserver.service.domain.exception.CarApplicationServiceException;
import com.traveladvisor.carserver.service.domain.exception.CarNotFoundException;
import com.traveladvisor.carserver.service.domain.port.input.event.FlightBookedEventListener;
import com.traveladvisor.carserver.service.message.mapper.CarMessageMapper;
import com.traveladvisor.common.domain.event.booking.FlightBookedEventPayload;
import com.traveladvisor.common.domain.vo.CarBookingStatus;
import com.traveladvisor.common.kafka.consumer.KafkaSingleMessageConsumer;
import com.traveladvisor.common.kafka.producer.KafkaMessageHelper;
import com.traveladvisor.common.message.constant.DebeziumOperator;
import debezium.booking.car_outbox.Envelope;
import debezium.booking.car_outbox.Value;
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
public class FlightBookedEventKafkaListener implements KafkaSingleMessageConsumer<Envelope> {

    private final FlightBookedEventListener flightBookedEventListener;
    private final CarMessageMapper carMessageMapper;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    @KafkaListener(
            id = "${kafka.config.consumer.car-consumer-group-id}",
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
        Value flightBookedEventAvroModel = message.getAfter();
        FlightBookedEventPayload flightBookedEventPayload = kafkaMessageHelper.getEventPayload(
                flightBookedEventAvroModel.getEventPayload(), FlightBookedEventPayload.class);

        try {
            switch (CarBookingStatus.valueOf(flightBookedEventPayload.getCarBookingStatus())) {
                // 차량 예약 상태가 PENDING 인 경우 항공권 예약을 처리합니다.
                case PENDING -> {
                    flightBookedEventListener.processCarBooking(carMessageMapper
                            .toCarBookingCommand(flightBookedEventPayload, flightBookedEventAvroModel));
                }
                // 차량 예약 상태가 CANCELLED 인 경우 항공권 예약을 취소 처리합니다.
                case CANCELLED -> {
                    flightBookedEventListener.compensateCarBooking(carMessageMapper
                            .toCarBookingCommand(flightBookedEventPayload, flightBookedEventAvroModel));
                }
                default -> log.warn("알 수 없는 차량 예약 상태입니다. BookingId: {}", flightBookedEventPayload.getBookingId());
            }
        } catch (DataAccessException ex) {
            SQLException sqlException = (SQLException) ex.getRootCause();

            if (sqlException != null && sqlException.getSQLState() != null &&
                    PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
                // 유니크 인덱스 제약 조건 위반 예외 발생의 경우 이미 다른 Consumer에서 처리 한 요청이기에 로그만 남깁니다.
                log.error("[PSQLState: {}] 유니크 제약 조건 예외가 발생했습니다. 이미 처리된 예약서 ID: {}",
                        sqlException.getSQLState(), flightBookedEventPayload.getBookingId());
            } else {
                // 그 외의 데이터베이스 예외는 밖으로 전파시킵니다. 이는 @KafkaListener가 자동으로 재시도를 수행하게 합니다.
                throw new CarApplicationServiceException("DataAccessException 예외가 발생했습니다. 예외 메시지: "
                        + ex.getMessage(), ex);
            }
        } catch (CarNotFoundException ex) {
            // 이 @KafkaListener는 예약서 정보가 없으면 메시지 처리를 다시 시도하지 않도록 합니다.
            log.error("예약서를 찾지 못했습니다. BookingId: {}", flightBookedEventPayload.getBookingId());
        }

    }

}
