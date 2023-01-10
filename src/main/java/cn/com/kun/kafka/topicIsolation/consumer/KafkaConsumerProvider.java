package cn.com.kun.kafka.topicIsolation.consumer;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public interface KafkaConsumerProvider {

    KafkaConsumer buildKafkaConsumer();

}
