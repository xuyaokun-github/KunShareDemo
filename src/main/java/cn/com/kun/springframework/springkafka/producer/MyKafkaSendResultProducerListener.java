package cn.com.kun.springframework.springkafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.ProducerListener;


/**
 * 消息监听器
 *
 * Created by xuyaokun On 2019/5/20 22:59
 * @desc:
 */
//@Component
public class MyKafkaSendResultProducerListener implements ProducerListener {

    private static Logger LOGGER = LoggerFactory.getLogger(MyKafkaSendResultProducerListener.class);

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

        if (exception != null){
            //有一类异常是无法在当下立刻补偿成功的，例如报文超限异常，需要修改服务端配置参数才能补偿成功
            LOGGER.error("出现kafka转发异常", exception);

            LOGGER.info("开始kafka转发异常补偿处理");
            //拿到报文(生产者写的是可能不一定是String类型)
            String source = String.valueOf(producerRecord.value());
            //写进补偿表，等待补偿处理

        }

    }

}
