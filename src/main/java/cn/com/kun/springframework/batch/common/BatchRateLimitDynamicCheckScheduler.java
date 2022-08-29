package cn.com.kun.springframework.batch.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 批处理动态限速调整器
 *
 * author:xuyaokun_kzx
 * date:2022/8/29
 * desc:
*/
public class BatchRateLimitDynamicCheckScheduler implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(BatchRateLimitDynamicCheckScheduler.class);

    private ScheduledExecutorService executorPool = Executors.newScheduledThreadPool(1);

    private JobRateLimitQueryFunction jobRateLimitQueryFunction;

    public BatchRateLimitDynamicCheckScheduler(JobRateLimitQueryFunction jobRateLimitQueryFunction) {
        this.jobRateLimitQueryFunction = jobRateLimitQueryFunction;
    }

    @Override
    public void run(String... args) throws Exception {

        LOGGER.info("启动批处理动态限速调整器");
        //动态调整的建议时延是60秒，因为要查库，不能太频繁
        executorPool.scheduleAtFixedRate(new RateLimitDynamicCheckTask(), 1, 10, TimeUnit.SECONDS);
    }

    private final class RateLimitDynamicCheckTask implements Runnable {

        RateLimitDynamicCheckTask() {

        }

        @Override
        public void run() {
            try {
                Set<String> jobIdCheckSet = BatchRateLimiterHolder.getJobIds();
                if (jobIdCheckSet.size() > 0){
                    jobIdCheckSet.forEach(jobId ->{

                        //根据jobId找到限流器，找到最新的限流值比对，假如不同，就调整，假如相同，do nothing
                        double newQps = jobRateLimitQueryFunction.queryRateLimit(jobId);
                        //内部做一个简单的缓存，避免重复调用BatchRateLimiterHolder
                        BatchRateLimiterHolder.adjust(jobId, newQps);
                    });
                }
            }catch (Throwable e){
                LOGGER.error("RateLimitDynamicCheckTask error", e);
            }
        }
    }

}
