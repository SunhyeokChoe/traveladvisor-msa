package com.traveladvisor.common.kafka.consumer.config;

import com.traveladvisor.common.kafka.config.KafkaCommonConfig;
import com.traveladvisor.common.kafka.config.KafkaConsumerCommonConfig;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
public class KafkaConsumerConfig<K extends Serializable, V extends SpecificRecordBase> {

    private final KafkaCommonConfig kafkaCommonConfig;
    private final KafkaConsumerCommonConfig kafkaConsumerCommonConfig;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCommonConfig.getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaConsumerCommonConfig.getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaConsumerCommonConfig.getValueDeserializer());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaConsumerCommonConfig.getAutoOffsetReset());
        props.put(kafkaCommonConfig.getSchemaRegistryUrlKey(), kafkaCommonConfig.getSchemaRegistryUrl());
        props.put(kafkaConsumerCommonConfig.getSpecificAvroReaderKey(), kafkaConsumerCommonConfig.getSpecificAvroReader());
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, kafkaConsumerCommonConfig.getSessionTimeoutMs());
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, kafkaConsumerCommonConfig.getHeartbeatIntervalMs());
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, kafkaConsumerCommonConfig.getMaxPollIntervalMs());
        props.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG,
            kafkaConsumerCommonConfig.getMaxPartitionFetchBytesDefault() *
                kafkaConsumerCommonConfig.getMaxPartitionFetchBytesBoostFactor());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, kafkaConsumerCommonConfig.getMaxPollRecords());
        return props;
    }

    @Bean
    public ConsumerFactory<K, V> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<K, V>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<K, V> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(kafkaConsumerCommonConfig.getBatchListener());
        factory.setConcurrency(kafkaConsumerCommonConfig.getConcurrencyLevel());
        factory.setAutoStartup(kafkaConsumerCommonConfig.getAutoStartup());
        factory.getContainerProperties().setPollTimeout(kafkaConsumerCommonConfig.getPollTimeoutMs());
        return factory;
    }

}
