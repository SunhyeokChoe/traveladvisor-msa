package com.traveladvisor.bookingserver.service.message.inbound.kafka;

import com.traveladvisor.bookingserver.service.domain.exception.BookingApplicationServiceException;
import com.traveladvisor.bookingserver.service.domain.exception.BookingNotFoundException;
import com.traveladvisor.bookingserver.service.domain.port.input.event.FlightBookingCompletedEventListener;
import com.traveladvisor.bookingserver.service.message.mapper.BookingMessageMapper;
import com.traveladvisor.common.domain.event.flight.FlightBookingCompletedEventPayload;
import com.traveladvisor.common.domain.vo.HotelBookingApprovalStatus;
import com.traveladvisor.common.kafka.consumer.KafkaListMessageConsumer;
import com.traveladvisor.common.kafka.producer.KafkaMessageHelper;
import com.traveladvisor.common.message.constant.DebeziumOperator;
import debezium.flight.booking_outbox.Envelope;
import debezium.flight.booking_outbox.Value;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLState;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class FlightBookingCompletedEventKafkaListener implements KafkaListMessageConsumer<Envelope> {

    private final FlightBookingCompletedEventListener flightBookingCompletedEventListener;
    private final BookingMessageMapper bookingMessageMapper;
    private final KafkaMessageHelper kafkaMessageHelper;

    @Override
    @KafkaListener(
            id = "${kafka.config.consumer.booking-consumer-group-id}",
            topics = "${kafka.topic.flight.inbound-topic-name}")
    public void receive(@Payload List<Envelope> messages,
                        @Header(KafkaHeaders.RECEIVED_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} 개의 항공권 예약 완료 또는 반려 응답을 수신했습니다.",
                messages.stream().filter(
                        message -> message.getBefore() == null &&
                                DebeziumOperator.CREATE.getValue().equals(message.getOp())
                ).toList().size());

        // "c" 즉, CREATE 작업일 경우 Outbox 테이블에 데이터가 삽입된 것이므로 이를 처리합니다.
        // 참고로 Debezium은 Outbox 패턴으로 CDC를 구현하는 것을 권장합니다. (Outbox Event Router: debezium.io/documentation/reference/0.9/configuration/outbox-event-router.html)
        messages.forEach(message -> {
            // Outbox WAL의 관찰 대상은 생성 트랜잭션이므로 CREATE OPERATOR 인 경우에만 처리할 수 있도록 합니다.
            if (message.getBefore() != null && !(DebeziumOperator.CREATE.getValue().equals(message.getOp()))) {
                return;
            }

            log.info("[IN-BOUND] 수신 메시지: {}", message);

            // 카프카 메시지로부터 Avro 메시지와 EventPayload를 추출합니다.
            Value flightBookingCompletedEventAvroModel = message.getAfter();
            FlightBookingCompletedEventPayload flightBookingCompletedEventPayload =
                    kafkaMessageHelper.getEventPayload(flightBookingCompletedEventAvroModel.getEventPayload(), FlightBookingCompletedEventPayload.class);

            try {
                switch (HotelBookingApprovalStatus.valueOf(flightBookingCompletedEventPayload.getFlightBookingApprovalStatus())) {
                    // 항공권 예약 승인 상태가 COMPLETED 인 경우 예약서에 항공권 예약 완료 상태를 저장하고 차량 예약을 요청합니다.
                    case COMPLETED -> {
                        flightBookingCompletedEventListener.processCarBooking(
                                bookingMessageMapper.toFlightBookingResponse(flightBookingCompletedEventPayload, flightBookingCompletedEventAvroModel));
                        log.info("항공권 예약이 성공적으로 처리되었습니다. 차량 예약 요청을 시도합니다. BookingId: {}", flightBookingCompletedEventPayload.getBookingId());
                    }
                    // 항공권 예약 승인 상태가 CANCELLED 혹은 FAILED 인 경우 예약서에 항공권 예약 취소 상태를 저장하고 호텔 예약 취소를 요청합니다.
                    case CANCELLED, FAILED -> {
                        flightBookingCompletedEventListener.compensateFlightBooking(
                                bookingMessageMapper.toFlightBookingResponse(flightBookingCompletedEventPayload, flightBookingCompletedEventAvroModel));
                        log.info("항공권 예약이 반려 처리되었습니다. BookingId: {}", flightBookingCompletedEventPayload.getBookingId());
                    }
                    default -> {
                        log.error("알 수 없는 호텔 예약 상태입니다. BookingId: {}", flightBookingCompletedEventPayload.getBookingId());
                    }
                }
            } catch (OptimisticLockingFailureException e) {
                // 낙관적 락을 통해 중복 메시지 처리를 방지합니다. 이미 처리된 메시지이므로 별도의 작업 없이 로그만 남기도록 합니다. 재시도가 필요하지 않습니다.
                log.error("BookingID {}에 대한 낙관적 락 예외가 발생했습니다. 이미 처리된 예약서 입니다.",
                        flightBookingCompletedEventPayload.getBookingId());
            } catch (DataAccessException ex) {
                SQLException sqlException = (SQLException) ex.getRootCause();

                if (sqlException != null && sqlException.getSQLState() != null &&
                        PSQLState.UNIQUE_VIOLATION.getState().equals(sqlException.getSQLState())) {
                    // 유니크 인덱스 제약 조건 위반 예외 발생의 경우 이미 다른 Consumer에서 처리 한 요청이기에 로그만 남깁니다.
                    log.error("[PSQLState: {}] 유니크 제약 조건 예외가 발생했습니다. 이미 처리된 예약서 ID: {}",
                            sqlException.getSQLState(), flightBookingCompletedEventPayload.getBookingId());
                } else {
                    // 그 외의 데이터베이스 예외는 밖으로 전파시킵니다. 이는 @KafkaListener가 자동으로 재시도를 수행하게 합니다.
                    throw new BookingApplicationServiceException("DataAccessException 예외가 발생했습니다. 예외 메시지: "
                            + ex.getMessage(), ex);
                }
            } catch (BookingNotFoundException ex) {
                // 이 @KafkaListener는 예약서 정보가 없으면 메시지 처리를 다시 시도하지 않도록 합니다.
                log.error("예약서를 찾지 못했습니다. BookingId: {}", flightBookingCompletedEventPayload.getBookingId());
            }
        });

    }

}
