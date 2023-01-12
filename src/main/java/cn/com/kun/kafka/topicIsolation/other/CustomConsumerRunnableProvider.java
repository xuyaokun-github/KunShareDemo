package cn.com.kun.kafka.topicIsolation.other;

import cn.com.kun.kafka.consumer.MsgCacheConsumeListener;
import cn.com.kun.kafka.topicIsolation.consumer.ConsumerRunnableProvider;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;

@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Component
public class CustomConsumerRunnableProvider implements ConsumerRunnableProvider {

    @Autowired
    MsgCacheConsumeListener msgCacheConsumeListener;

    @Override
    public Runnable getConsumerRunnable(KafkaConsumer consumer, String topic, Executor myKafkaMsgExecutor) {

        return msgCacheConsumeListener.buildConsumerRunnable(consumer, topic, myKafkaMsgExecutor);
    }

}
