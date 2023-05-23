package cn.com.kun.kafka.autoSwitch.other;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.kafka.msg.MsgCacheTopicMsg;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * 生产者demo
 *
 * author:xuyaokun_kzx
 * desc:
*/
@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Service
public class KafkaAutoSwitchProducerDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaAutoSwitchProducerDemoService.class);

    @Autowired
    @Qualifier("autoSwitchKafkaProducer")
    private Producer<String, String> kafkaProducer;

    /**
     * 仅负责生产Topic2:
     */
    @Autowired
    @Qualifier("autoSwitchKafkaProducer2")
    private Producer<String, String> kafkaProducer2;

    /**
     * 假如主题不存在，会主动创建
     *
     * @param msgCacheTopicMsg
     * @param topicName
     */
    public void produceForAutoSwitch(MsgCacheTopicMsg msgCacheTopicMsg, String topicName) {

        //KafkaProducer类发送数据，kafka Producer是线程安全的，可以在多个线程之间共享生产者实例
        // -- 同步发送消息
        kafkaProducer.send(new ProducerRecord<String, String>(topicName, JacksonUtils.toJSONString(msgCacheTopicMsg)));

        //或者用Future的方式
//        ProducerRecord<String, String> syncRecord = new ProducerRecord<>(topicName, "Kafka_Products", "测试"); //Topic Key Value
//        try{
//            Future future = kafkaProducer.send(syncRecord);
//            future.get();//不关心是否发送成功，则不需要这行。
//        } catch(Exception e) {
//            e.printStackTrace();//连接错误、No Leader错误都可以通过重试解决；消息太大这类错误kafkaProducer不会进行任何重试，直接抛出异常
//        }

        //不需要关闭
//        kafkaProducer.close();
    }


    public void produceForAutoSwitchForTopic2(MsgCacheTopicMsg msgCacheTopicMsg, String topicName) {

        //KafkaProducer类发送数据，kafka Producer是线程安全的，可以在多个线程之间共享生产者实例
        // -- 同步发送消息
        kafkaProducer2.send(new ProducerRecord<String, String>(topicName, JacksonUtils.toJSONString(msgCacheTopicMsg)));

        //或者用Future的方式
//        ProducerRecord<String, String> syncRecord = new ProducerRecord<>(topicName, "Kafka_Products", "测试"); //Topic Key Value
//        try{
//            Future future = kafkaProducer.send(syncRecord);
//            future.get();//不关心是否发送成功，则不需要这行。
//        } catch(Exception e) {
//            e.printStackTrace();//连接错误、No Leader错误都可以通过重试解决；消息太大这类错误kafkaProducer不会进行任何重试，直接抛出异常
//        }

        //不需要关闭
//        kafkaProducer.close();
    }
}
