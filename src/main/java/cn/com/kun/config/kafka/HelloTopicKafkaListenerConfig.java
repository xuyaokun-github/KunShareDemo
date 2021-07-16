package cn.com.kun.config.kafka;

import cn.com.kun.kafka.consumer.HelloTopicMsgProcessor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.Optional;


/**
 * 消息消费者监听器配置
 *
 * @author xuyaokun
 * @date 2020/3/16 11:22
 */
//@Configuration
public class HelloTopicKafkaListenerConfig {

    private static Logger LOGGER = LoggerFactory.getLogger(HelloTopicKafkaListenerConfig.class);

    @Autowired
    private HelloTopicMsgProcessor helloTopicMsgProcessor;

    /**
     * 可以同时监听多个topic
     * 假如不指定组ID,默认组ID为空，在kafka-manager端监控不到客户端信息
     * @param record
     */
    //下面只监听了一个主题，hello
//    @KafkaListener(topics = {"hello-topic"})
//    @KafkaListener(topics = {"hello-topic"}, groupId = "kunsharedemo")
    public void listen(ConsumerRecord<?, ?> record) {
        //下面这种主要就是利用jdk1.8的Optional做下非空判断而已
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            LOGGER.info("----------------- record =" + record);
            LOGGER.info("接收到message：{}", message);
        }

    }

    /**
     * 根据连接池，创建出监听器（相当于一个消费者）
     * id属性可以自动生成，标记每一个客户端，也可以主动指定 id = "ackComsumerID"
     * @param record
     * @param ack
     */
    @KafkaListener(topics = "hello-topic", groupId = "kunsharedemo", containerFactory = "kafkaAckContainerFactory")
    public void ackListener(ConsumerRecord record, Acknowledgment ack) {

        LOGGER.info("ackListener receive : " + record.value());
        helloTopicMsgProcessor.process(record, ack);
    }

}
