package cn.com.kun.kafka.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class KafkaThreadPoolTaskExecutorConfig {

    /**
     * @return
     */
    @Bean("myKafkaMsgExecutor")
    public Executor myKafkaMsgExecutor() {

        // 对线程池进行包装，使之支持traceId透传
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(16);
        executor.setThreadNamePrefix("KafkaMsg-Thread-");
        executor.setQueueCapacity(200);//默认是LinkedBlockingQueue
        executor.initialize();
        return executor;
    }


}
