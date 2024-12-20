package com.traveladvisor.common.kafka.producer.config;

import com.traveladvisor.common.kafka.config.KafkaCommonConfig;
import com.traveladvisor.common.kafka.config.KafkaProducerCommonConfig;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 제네릭 타입 K(key): 직렬화 가능해야 하므로 Serializable 인터페이스를 확장합니다.
 * 제네릭 타입 V(value): Avro를 처리할 수 있도록 SpecificRecordBase 추상 클래스를 확장합니다.
 * @param <K>
 * @param <V>
 */
@RequiredArgsConstructor
@Configuration
public class KafkaProducerConfig<K extends Serializable, V extends SpecificRecordBase> {

    private final KafkaCommonConfig kafkaCommonConfig;
    private final KafkaProducerCommonConfig kafkaProducerCommonConfig;

    @Bean
    public Map<String, Object> producerConfig() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCommonConfig.getBootstrapServers());
        props.put(kafkaCommonConfig.getSchemaRegistryUrlKey(), kafkaCommonConfig.getSchemaRegistryUrl());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, kafkaProducerCommonConfig.getKeySerializerClass());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, kafkaProducerCommonConfig.getValueSerializerClass());
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerCommonConfig.getBatchSize() *
            kafkaProducerCommonConfig.getBatchSizeBoostFactor());
        props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerCommonConfig.getLingerMs());
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerCommonConfig.getCompressionType());
        props.put(ProducerConfig.ACKS_CONFIG, kafkaProducerCommonConfig.getAcks());
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerCommonConfig.getRequestTimeoutMs());
        props.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerCommonConfig.getRetryCount());
        return props;
    }

    @Bean
    public ProducerFactory<K, V> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<K, V> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

}
