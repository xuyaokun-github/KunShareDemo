package cn.com.kun.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;


/**
 *
 * 消息消费者
 *
 * @author xuyaokun
 * @date 2020/3/16 11:22
 */
//@Component
public class KafkaReceiver {

    private static Logger logger = LoggerFactory.getLogger(KafkaReceiver.class);

    //可以同时监听多个topic
    //下面只监听了一个主题，hello
    @KafkaListener(topics = {"hello-topic"})
    public void listen(ConsumerRecord<?, ?> record) {
        //下面这种主要就是利用jdk1.8的Optional做下非空判断而已
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            logger.info("----------------- record =" + record);
            logger.info("------------------ message =" + message);
        }

    }


}
