package cn.com.kun.springframework.springkafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;


/**
 * 消息回调处理器
 * 监听器不能定义多个
 *
 * Created by xuyaokun On 2019/5/20 22:59
 * @desc:
 */
//@Component
public class MyKafkaSendResultProducerListener2 implements ProducerListener {

    private static Logger LOGGER = LoggerFactory.getLogger(MyKafkaSendResultProducerListener2.class);

    @Override
    public void onSuccess(ProducerRecord producerRecord, RecordMetadata recordMetadata) {

        LOGGER.info("Kafka监听器 Message send success : {}", producerRecord.toString());

        //回调时，还可以输出完整的消息内容，知道之前发送的是什么
        LOGGER.info("Kafka监听器 收到完整消息内容：{}", producerRecord.value());

        //在这里还可以获取Header头
        Headers headers = producerRecord.headers();
        Header[] headersArr = headers.toArray();


    }

    @Override
    public void onError(ProducerRecord producerRecord, Exception exception) {
//        LOGGER.info("Kafka监听器 Message send error : {}", producerRecord.toString());
        LOGGER.error("Kafka监听器 Message send error", exception);

    }
}
