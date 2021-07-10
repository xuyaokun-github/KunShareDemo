package cn.com.kun.schedule.threadpooltaskexecutor;

import cn.com.kun.common.utils.ThreadMdcUtil;
import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

@Configuration
public class ThreadPoolTaskExecutorConfig {

    /**
     * 不支持traceId传递
     * 实践发现，ThreadPoolTaskExecutor默认已经支持了traceId的传递！
     * 因为它用了org.springframework.cloud.sleuth.instrument.async.TraceRunnable#run()
     * 往池子中提交的任务自动被TraceRunnable封装了
     * @return
     */
    @Bean("myFirstTaskExecutor")
    public Executor myFirstTaskExecutor() {

        // 对线程池进行包装，使之支持traceId透传
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.initialize();
        return executor;
    }

    /**
     * 支持traceId传递
     * @return
     */
    @Bean("mySecondTaskExecutor")
    public Executor mySecondTaskExecutor() {

        // 对线程池进行包装，使之支持traceId透传
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor() {
            @Override
            public <T> Future<T> submit(Callable<T> task) {
                // 传入线程池之前先复制当前线程的MDC
                return super.submit(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
            }
            @Override
            public void execute(Runnable task) {
                super.execute(ThreadMdcUtil.wrap(task, MDC.getCopyOfContextMap()));
            }
        };
        executor.setCorePoolSize(1);
        executor.initialize();
        return executor;
    }



}
