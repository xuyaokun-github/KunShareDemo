package cn.com.kun.kafka.dynamicConsume.extend;

import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 * 消费者构造器接口
 *
 * author:xuyaokun_kzx
 * date:2023/7/12
 * desc:
*/
public interface KafkaConsumerBuilder {

    /**
     * 返回一个消费者对象（未订阅主题）
     * @return
     */
    KafkaConsumer buildKafkaConsumer();

}
