package cn.com.kun.springframework.batch.common;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 批处理限流器管理类
 * --保存所有限流器
 *
 * author:xuyaokun_kzx
 * date:2021/8/29
 * desc:
*/
public class BatchRateLimiterHolder {

    private final static Logger LOGGER = LoggerFactory.getLogger(BatchRateLimiterHolder.class);

    /**
     * 全局限流器-每秒产生1000个令牌
     */
    private static RateLimiter DEFAULT_BATCH_RATE_LIMITER = RateLimiter.create(1000);

    /**
     * 全局限流器-每秒产生1个令牌
     */
    private static RateLimiter DEFAULT_BATCH_RATE_LIMITER_SLOW = RateLimiter.create(1);

    /**
     * 一个job对应一个ID(或者叫taskId)
     */
    private static Map<String, RateLimiter> jobRateLimiterMap = new ConcurrentHashMap();


    /**
     * 获取限流器
     * @param jobId
     * @return
     */
    public static RateLimiter getRateLimiter(String jobId) {

        RateLimiter rateLimiter = jobRateLimiterMap.get(jobId);
        if (rateLimiter == null){
            return DEFAULT_BATCH_RATE_LIMITER;
        }
        return rateLimiter;
    }

    /**
     * 注册限流器
     * （假如要实现动态调整，需要定时刷新限流器，重新注册）
     *
     * @param jobId
     * @param permitsPerSecond
     */
    public static void registerRateLimiter(String jobId, double permitsPerSecond) {

        if (jobRateLimiterMap.size() > 10000){
            //
            LOGGER.info("注册限流器数量超过10000，请检查是否存在内存泄漏，限流功能将会受影响");
            return;
        }

        RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond);
        jobRateLimiterMap.put(jobId, rateLimiter);
    }

    /**
     * 移除限流器
     * @param jobId
     */
    public static void destroyRateLimiter(String jobId) {

        jobRateLimiterMap.remove(jobId);
    }

    /**
     * 调整限流值
     * @param jobId
     * @param newQps
     */
    public static void adjust(String jobId, double newQps) {

        RateLimiter rateLimiter = jobRateLimiterMap.get(jobId);
        if (rateLimiter != null && rateLimiter.getRate() != newQps && newQps > 0){
            //调整限流值
            rateLimiter.setRate(newQps);
        }else {
            //假如限流器已经被销毁了，或者相等，无需调整

        }
    }


    public static Set<String> getJobIds() {
        return jobRateLimiterMap.keySet();
    }
}
