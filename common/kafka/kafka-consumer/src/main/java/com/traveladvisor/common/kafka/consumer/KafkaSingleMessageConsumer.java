package com.traveladvisor.common.kafka.consumer;

import org.apache.avro.specific.SpecificRecordBase;

/**
 * 여러 메시지를 List 형태로 전달받아 처리하는 스펙을 갖는 KafkaConsumer 인터페이스를 구현한 카프카 리스너는
 * 오프셋 커밋 타임아웃 예외가 마구 발생 할 수 있습니다. 따라서 예약 처리는 단일 메시지로 처리하도록 합니다.
 * 이는 이전에 처리한 항목을 재처리 시도하는 것을 방지할 수 있습니다.
 *
 * ※ 설정에서 batch-listener를 false로 지정해야 합니다.
 */
public interface KafkaSingleMessageConsumer<T extends SpecificRecordBase> {

    void receive(T message, String key, Integer partition, Long offset);

}
