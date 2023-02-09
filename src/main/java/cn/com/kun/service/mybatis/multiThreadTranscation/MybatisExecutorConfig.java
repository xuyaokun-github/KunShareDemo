package cn.com.kun.service.mybatis.multiThreadTranscation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**  线程池配置
 * @version V1.0
 */
public class MybatisExecutorConfig {

    private static int maxPoolSize = Runtime.getRuntime().availableProcessors();
    private volatile static ExecutorService executorService;
    public static ExecutorService getThreadPool() {
        if (executorService == null){
            synchronized (MybatisExecutorConfig.class){
                if (executorService == null){
                    executorService =  newThreadPool();
                }
            }
        }
        return executorService;
    }

    private static ExecutorService newThreadPool(){
        int queueSize = 500;
        int corePool = Math.min(5, maxPoolSize);
        return new ThreadPoolExecutor(corePool, maxPoolSize, 10000L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(queueSize),new ThreadPoolExecutor.AbortPolicy());
    }
    private MybatisExecutorConfig(){}
}
