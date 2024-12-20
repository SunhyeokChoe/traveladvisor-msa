package com.traveladvisor.common.kafka.producer.service.impl;

import com.traveladvisor.common.kafka.producer.service.KafkaProducer;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.Serializable;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V> {

    private final KafkaTemplate<K, V> kafkaTemplate;

    @Override
    public void send(String topicName, K key, V message, ListenableFutureCallback<SendResult<K, V>> callback) {
        log.info("메시지 = {}를 토픽 = {}으로 전송합니다.", message, topicName);

        /*
         * Kafka Producer의 send 메서드는 논블로킹 비동기 호출이므로 결과를 즉시 반환합니다.
         * 그러므로 나중에 응답이 왔을 때 콜백 메서드가 실행될 수 있도록 콜백을 등록해 둡니다.
         */
        try {
            ListenableFuture<SendResult<K, V>> kafkaListenableFuture =
                    (ListenableFuture<SendResult<K, V>>) kafkaTemplate.send(topicName, key, message);

            kafkaListenableFuture.addCallback(callback);
        } catch (KafkaException e) {
            log.error("Kafka 프로듀서에서 오류 발생 - 키: {}, 메시지: {}, 예외: {}.", key, message, e.getMessage());
            throw new KafkaException("Kafka 프로듀서에서 오류 발생 - 키: " + key + ", 메시지: " + message + ", 예외: " + e.getMessage() + ".");
        }
    }

    /**
     * 응용 프로그램이 종료될 때 종료 관련 메시지를 로그로 남긴 후 kafkaTemplate을 명시적으로 GC 대상이 되도록 합니다.
     * @PreDestroy 이 애너테이션은 응용 프로그램이 종료될 때 특정 메서드가 실행될 수 있도록 해줍니다.
     */
    @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            log.info("Kafka Producer를 종료합니다.");
            kafkaTemplate.destroy();
        }
    }

}
