package com.traveladvisor.common.kafka.consumer;

import org.apache.avro.specific.SpecificRecordBase;

import java.util.List;

public interface KafkaListMessageConsumer<T extends SpecificRecordBase> {

    void receive(List<T> messages, List<String> keys, List<Integer> partitions, List<Long> offsets);

}
