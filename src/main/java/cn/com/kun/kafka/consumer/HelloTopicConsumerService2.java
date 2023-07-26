package cn.com.kun.kafka.consumer;

import cn.com.kun.kafka.config.KafkaConsumerProperties;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * kafka消费者（单线程拉取，多线程消费）
 */
@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Component
public class HelloTopicConsumerService2 {

    private final static Logger LOGGER = LoggerFactory.getLogger(HelloTopicConsumerService2.class);

    @Autowired
    @Qualifier("helloTopicKafkaConsumer")
    private KafkaConsumer consumer;

    private long maxWaitTime;

    @Autowired
    @Qualifier("myKafkaMsgExecutor")
    Executor myKafkaMsgExecutor;

    @Autowired
    MsgCacheMsgProcessor msgCacheMsgProcessor;

    @Autowired
    KafkaConsumerProperties kafkaConsumerProperties;

    @PostConstruct
    public void init() {

        maxWaitTime = getMaxWaitTime(kafkaConsumerProperties.getMaxPollIntervalMs());

        new Thread(() -> {
            while (true) {
                try {
                    //拉取消息
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000).toMillis());
                    if (records.count() > 0) {
                        LOGGER.info("本次poll条数：{}", records.count());

                        //创建消息记录的装饰类
                        List<RecordWrapper> recordWrapperList = new ArrayList<>();
                        records.forEach(record ->{
                            RecordWrapper recordWrapper = new RecordWrapper(record);
                            recordWrapperList.add(recordWrapper);

                            if (record.value().contains("interrupt")){
                                //模拟消费异常，然后打上中断标记，看会发生什么？
                                throw new RuntimeException();
                            }

                        });

                        while(true) {
                            //创建闭锁
                            CountDownLatch countDownLatch = new CountDownLatch(records.count());
                            AtomicBoolean isAllSuccess = new AtomicBoolean(true);
                            recordWrapperList.forEach(recordWrapper -> {
                                recordWrapper.setCountDownLatch(countDownLatch);
                                recordWrapper.setIsAllSuccess(isAllSuccess);
                            });
                            for (RecordWrapper recordWrapper : recordWrapperList) {
                                //提交任务到线程池
                                myKafkaMsgExecutor.execute(new HelloTopicMsgRunnable(msgCacheMsgProcessor, recordWrapper));
                            }
                            //唤醒(假如超过5分钟，还没执行完，必须要重新poll，不然会触发重平衡！)
                            boolean isSuccessProcess = true;
                            try {
                                countDownLatch.await();
//                                isSuccessProcess = countDownLatch.await(maxWaitTime, TimeUnit.MILLISECONDS);
                            }catch (InterruptedException e){
                                //出现消费速度慢的情况，拉取一批数据回来，在5分钟内没有被处理完
                                isSuccessProcess = false;
                            }
                            if (!isAllSuccess.get()){
                                //判断是否致命异常，假如有，这一批消息不能提交
                                LOGGER.error("出现重要异常，消息可能丢失！");
                                //这种情况，程序假如继续往下，这批消息肯定会丢，要么终止该消费者实例，要么继续尝试
                                Thread.sleep(2000);//睡眠，继续重试
                                //这里可以做个优化，处理成功的消息，可以给个标记
                                continue;
                            }else {
                                //假如都执行成功了或者入了补偿表
                                //所有消息处理完毕之后，调用手动提交
                                //手动提交
                                if (!isSuccessProcess){
                                    LOGGER.warn("countDownLatch被超时唤醒，消费速度出现异常，请检查链路耗时");
                                }else {
                                    LOGGER.info("正常消费完{}条消息", records.count());
                                }
                                commit();
                                break;
                            }
                        }
                    } else {
                        //没有拉取到的时候，不要调提交方法，会报异常
                    }

                } catch (Exception e) {
                    LOGGER.error("消费异常", e);
                    //打上中断标记（反例代码）
                    Thread.currentThread().interrupt();
                    if (Thread.interrupted()){
                        //清理中断标记
                        LOGGER.error("出现中断异常", e);
                    }
                }
            }
        }, "HelloTopic-KafkaConsumer-Thread").start();
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
}
