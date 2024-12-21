package com.traveladvisor.common.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveladvisor.common.domain.outbox.OutboxStatus;
import com.traveladvisor.common.kafka.producer.exception.KafkaProducerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.function.BiConsumer;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaMessageHelper {

    private final ObjectMapper objectMapper;

    public <T> T getEventPayload(String payload, Class<T> outputType) {
        try {
            return objectMapper.readValue(payload, outputType);
        } catch (JsonProcessingException ex) {
            log.error("객체 '{}'를 읽을 수 없습니다.", outputType.getName(), ex);
            throw new KafkaProducerException("객체 " + outputType.getName() + "를 읽을 수 없습니다. " + ex.getMessage());
        }
    }

    public <T, U> ListenableFutureCallback<SendResult<String, T>> getKafkaCallback(
            String responseTopicName,
            T avroModel,
            U outboxMessage,
            BiConsumer<U, OutboxStatus> outboxCallback,
            String bookingId,
            String avroModelName) {
        return new ListenableFutureCallback<SendResult<String, T>>() {

            /**
             * 카프카 프로듀서가 실패 응답을 받으면 Outbox 테이블의 OutboxStatus 상태를 FAILED로 변경합니다.
             */
            @Override
            public void onFailure(Throwable ex) {
                log.error("'{}'을 전송하는 중 오류가 발생했습니다. 메시지: {}, Outbox 타입: {}, 대상 토픽: {}.",
                        avroModelName, avroModel.toString(), outboxMessage.getClass().getName(), responseTopicName, ex);

                outboxCallback.accept(outboxMessage, OutboxStatus.FAILED);
            }

            /**
             * 카프카 프로듀서가 성공 응답을 받으면 Outbox 테이블의 OutboxStatus 상태를 COMPLETED로 변경합니다.
             */
            @Override
            public void onSuccess(SendResult<String, T> result) {
                RecordMetadata metadata = result.getRecordMetadata();

                // TODO: Saga, Outbox 패턴을 활용한 트랜잭션 보상 작업이 없으므로 일단 로그로만 남기도록 했다. 추후 Saga 및 Outbox 패턴으로 리팩터링 하겠다.
                log.info("Kafka에서 ID: {}에 대한 성공적인 응답을 받았습니다. 토픽: {} 파티션: {} 오프셋: {} 타임스탬프: {}",
                        bookingId,
                        metadata.topic(),
                        metadata.partition(),
                        metadata.offset(),
                        metadata.timestamp());

                outboxCallback.accept(outboxMessage, OutboxStatus.COMPLETED);
            }
        };
    }

}
