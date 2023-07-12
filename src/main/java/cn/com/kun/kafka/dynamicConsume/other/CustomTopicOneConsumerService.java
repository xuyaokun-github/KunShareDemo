package cn.com.kun.kafka.dynamicConsume.other;

import cn.com.kun.kafka.config.KafkaConsumerProperties;
import cn.com.kun.kafka.consumer.MsgCacheMsgProcessor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.Duration;
import java.util.concurrent.Executor;

/**
 * kafka消费者（单线程拉取，多线程消费）
 */
//@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
//@Component
public class CustomTopicOneConsumerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomTopicOneConsumerService.class);

    /**
     * 假如不定义一个独立的消费组，在设置为手动提交且不提交位移的时候，会出现Attempt to heartbeat failed for since member id consumer-4-9d754ae1-c959-4632-adaf-73a26db64a02 is not valid.
     *
     */
    @Autowired
    @Qualifier("customTopicOneKafkaConsumer")
    private KafkaConsumer consumer;

    @Autowired
    @Qualifier("myKafkaMsgExecutor")
    Executor myKafkaMsgExecutor;

    @Autowired
    MsgCacheMsgProcessor msgCacheMsgProcessor;

    @Autowired
    KafkaConsumerProperties kafkaConsumerProperties;

    private boolean consumeSwitch = true;

//    @PostConstruct
    public void init() {

        new Thread(() -> {

            /*
                这是一个反例代码
             */
            while (true) {
                try {
                    //拉取消息
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000).toMillis());
                    if (records.count() > 0) {

                        if(consumeSwitch){
                            LOGGER.info("本次poll条数：{}", records.count());
                            //模拟业务处理
                            records.forEach(record ->{
                                LOGGER.info("处理消息内容：{}", record.toString());
                            });

                            commit();
                        }else {
                            LOGGER.info("消费开关未开启（不在消费时段），不做消息处理");
                        }

                    } else {
                        //没有拉取到的时候，不要调提交方法，会报异常
                    }

                } catch (Exception e) {
                    LOGGER.error("消费异常", e);
                }
            }
        }, "CustomTopicOneConsumerService-KafkaConsumer-Thread").start();
    }

    /**
     * 获取最大等待时长
     * @param maxPollIntervalMs 单位毫秒
     * @return
     */
    private long getMaxWaitTime(String maxPollIntervalMs) {

        //假设设置为5分钟，则设置为4分钟，假设设置设置为1000毫秒，则设置为800毫秒，
        //乘以0.8
        long value = Long.parseLong(maxPollIntervalMs);
        long res = (long) (value * 0.8);
        return res;
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

    public boolean isConsumeSwitch() {
        return consumeSwitch;
    }

    public void setConsumeSwitch(boolean consumeSwitch) {
        this.consumeSwitch = consumeSwitch;
    }
}
