package cn.com.kun.kafka.topicIsolation.consumer;

import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.concurrent.Executor;

public interface ConsumerRunnableProvider {

    Runnable getConsumerRunnable(KafkaConsumer consumer, String topic, Executor myKafkaMsgExecutor);

}
