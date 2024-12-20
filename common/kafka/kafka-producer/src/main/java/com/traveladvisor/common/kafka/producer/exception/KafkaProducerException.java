package com.traveladvisor.common.kafka.producer.exception;

// TODO: Global Exception Handler에서 공통으로 처리될 수 있도록 KafkaProducerException를 추가해야 합니다.
public class KafkaProducerException extends RuntimeException {

    public KafkaProducerException(String message) {
        super(message);
    }

}
