package cn.com.kun.kafka.dynamicConsume.other;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;

/**
 * kafka消费者（单线程拉取，多线程消费）
 */
@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Component
public class CustomTopicOneConsumerService3 {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomTopicOneConsumerService3.class);

    /**
     * 假如不定义一个独立的消费组，在设置为手动提交且不提交位移的时候，会出现Attempt to heartbeat failed for since member id consumer-4-9d754ae1-c959-4632-adaf-73a26db64a02 is not valid.
     *
     */
    //引入动态消费的消费者
    @Autowired
    @Qualifier("dynamicKafkaConsumer")
    private Consumer consumer;

    @PostConstruct
    public void init() {

        new Thread(() -> {

            while (true) {
                try {
                    //拉取消息
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
                    if (records != null && records.count() > 0) {
                        LOGGER.info("本次poll条数：{}", records.count());
                        //模拟业务处理
                        records.forEach(record ->{
                            LOGGER.info("处理消息内容：{}", record.toString());
                        });
                        commit();
                    } else {
                        //records可能返回空
                        //没有拉取到的时候，不要调提交方法，会报异常
                    }

                } catch (Exception e) {
                    LOGGER.error("消费异常", e);
                }
            }
        }, "CustomTopicOneConsumerService-KafkaConsumer-Thread").start();
    }

    public void commit(){
        try {
            consumer.commitAsync();
        } catch (Exception e) {
            LOGGER.error("异步提交异常", e);
            consumer.commitSync();
            LOGGER.info("手动同步提交完成");
        }
    }

}
