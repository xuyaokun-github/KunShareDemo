package cn.com.kun.kafka.topicIsolation.other;

import cn.com.kun.kafka.topicIsolation.consumer.KafkaConsumeExecutorProvider;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Component
public class CustomKafkaConsumeExecutorProviderImpl implements KafkaConsumeExecutorProvider {

    @Override
    public Executor buildKafkaConsumeExecutor(String topic) {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setThreadNamePrefix(topic.toLowerCase() + "-KafkaConsumeExecutor-Thread-");
        executor.setQueueCapacity(200);//默认是LinkedBlockingQueue
        //饱和策略，为了避免丢弃，建议还是设置成CallerRunsPolicy
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
