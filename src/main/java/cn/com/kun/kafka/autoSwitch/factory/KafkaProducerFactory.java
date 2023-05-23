package cn.com.kun.kafka.autoSwitch.factory;

import org.apache.kafka.clients.producer.Producer;

public interface KafkaProducerFactory {


    Producer buildProducer(String topic, String address);

}
