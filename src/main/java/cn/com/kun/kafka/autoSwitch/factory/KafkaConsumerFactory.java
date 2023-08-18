package cn.com.kun.kafka.autoSwitch.factory;

import org.apache.kafka.clients.consumer.Consumer;

/**
 * 消费者工厂
 *
 * author:xuyaokun_kzx
 * date:2023/8/14
 * desc:
*/
public interface KafkaConsumerFactory {

    Consumer buildConsumer(String topic, String address);

}
