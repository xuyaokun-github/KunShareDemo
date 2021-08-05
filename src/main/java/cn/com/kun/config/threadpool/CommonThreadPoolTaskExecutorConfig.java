package cn.com.kun.config.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class CommonThreadPoolTaskExecutorConfig {

    /**
     * 定义一个业务公共线程池
     * （建议不要这样用，每一个业务最好独立用一个线程池，这样可以避免业务间的互相影响）
     * 这里我为了做演示使用
     *
     * @return
     */
    @Bean("myBizCommonExecutor")
    public Executor myBizCommonExecutor(CustomRejectedExecutionHandler customRejectedExecutionHandler) {

        // 对线程池进行包装，使之支持traceId透传
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(4);
        executor.setThreadNamePrefix("myBizCommonExecutor-Thread-");
        executor.setQueueCapacity(2);//默认是LinkedBlockingQueue
        //默认的饱和策略是
        executor.setRejectedExecutionHandler(customRejectedExecutionHandler);
        return executor;
    }


}
