package cn.com.kun.kafka.topicIsolation.consumer;

import java.util.concurrent.Executor;

public interface KafkaConsumeExecutorProvider {

    Executor buildKafkaConsumeExecutor(String topic);

}
