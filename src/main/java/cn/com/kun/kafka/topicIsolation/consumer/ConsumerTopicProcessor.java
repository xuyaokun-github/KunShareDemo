package cn.com.kun.kafka.topicIsolation.consumer;

import cn.com.kun.kafka.topicIsolation.TopicBeanFactory;
import cn.com.kun.kafka.topicIsolation.bean.TopicBean;
import cn.com.kun.kafka.topicIsolation.properties.KafkaTopicIsolationProperties;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 消费端主题拆分处理类-消费端使用
 *
 * 主要作用：
 * 1.创建消费者
 * 2.分配线程池
 *
 * author:xuyaokun_kzx
 * date:2022/12/30
 * desc:
*/
@Component
public class ConsumerTopicProcessor implements ApplicationContextAware, InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConsumerTopicProcessor.class);

    @Autowired
    private KafkaTopicIsolationProperties kafkaTopicIsolationProperties;

    private ConsumerRunnableProvider consumerRunnableProvider;

    private KafkaConsumeExecutorProvider kafkaConsumeExecutorProvider;

    private KafkaConsumerProvider kafkaConsumerProvider;

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {

        //假如消费者功能没启用，不初始化实现类
        if (!kafkaTopicIsolationProperties.isConsumerEnabled()){
            return;
        }

        //必须提供一个KafkaConsumerProvider实现
        kafkaConsumerProvider = applicationContext.getBean(KafkaConsumerProvider.class);
        Assert.notNull(kafkaConsumerProvider, String.format("KafkaConsumerProvider实现为空,必须实现一个KafkaConsumerProvider Bean"));

        //必须提供一个KafkaConsumerProvider实现
        kafkaConsumeExecutorProvider = applicationContext.getBean(KafkaConsumeExecutorProvider.class);
        Assert.notNull(kafkaConsumerProvider, String.format("KafkaConsumeExecutorProvider实现为空,必须实现一个KafkaConsumeExecutorProvider Bean"));

        //必须提供一个KafkaConsumerProvider实现
        consumerRunnableProvider = applicationContext.getBean(ConsumerRunnableProvider.class);
        Assert.notNull(consumerRunnableProvider, String.format("ConsumerRunnableProvider实现为空,必须实现一个ConsumerRunnableProvider Bean"));
    }

    public void start(){

        //假如消费者功能没启用，不启动消费线程
        if (!kafkaTopicIsolationProperties.isConsumerEnabled()){
            return;
        }

        Map<String, Map<String, TopicBean>> topicBeansMap = TopicBeanFactory.getTopicBeansMap();

        topicBeansMap.forEach((k, v)-> {
            v.forEach((k1,v1)->{
                //遍历所有主题
                String topic = v1.buildTopicName();
                //创建消费者、创建线程池
                //KafkaConsumer和Executor 有必要放入spring容器吗？是否有必要，由使用方决定
                KafkaConsumer consumer = kafkaConsumerProvider.buildKafkaConsumer();
                Executor myKafkaMsgExecutor = kafkaConsumeExecutorProvider.buildKafkaConsumeExecutor(topic);
                Runnable runnable = consumerRunnableProvider.getConsumerRunnable(consumer, topic, myKafkaMsgExecutor);
                //开启消费线程
                startConsumeThread(runnable, topic.toLowerCase() + "-consumer-thread");
            });
        });

        //默认主题，也需要分配一个消费线程
        String topic = kafkaTopicIsolationProperties.getDefaultTopic();
        KafkaConsumer consumer = kafkaConsumerProvider.buildKafkaConsumer();
        Executor myKafkaMsgExecutor = buildKafkaConsumeExecutor(topic);

        Runnable runnable = consumerRunnableProvider.getConsumerRunnable(consumer, topic, myKafkaMsgExecutor);
        //开启消费线程
        startConsumeThread(runnable, topic.toLowerCase() + "-consumer-thread");

    }

    private Executor buildKafkaConsumeExecutor(String topicName) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setThreadNamePrefix(topicName.toLowerCase() + "-KafkaConsumeExecutor-Thread-");
        executor.setQueueCapacity(200);//默认是LinkedBlockingQueue
        executor.initialize();
        return executor;
    }

    private void startConsumeThread(Runnable runnable, String threadName) {
        new Thread(runnable, threadName).start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
