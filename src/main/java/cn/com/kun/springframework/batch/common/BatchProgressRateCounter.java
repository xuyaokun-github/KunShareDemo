package cn.com.kun.springframework.batch.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 批处理进度统计器
 * (只能针对Step多线程线程时使用)
 * Created by xuyaokun On 2022/6/18 13:51
 * @desc:
 */
public class BatchProgressRateCounter {

    private static Map<String, AtomicLong> lineCountMap = new ConcurrentHashMap<>();

    /**
     * 初始化计数器，在job监听器调用
     */
    public static void initCounter(String jobInstanceId) {

        lineCountMap.put(jobInstanceId, new AtomicLong(0));
    }

    /**
     * 移除计数器，在job监听器调用
     */
    public static void removeCounter(String jobInstanceId) {

        lineCountMap.remove(jobInstanceId);
    }

    /**
     * Step阶段调用
     * @param jobInstanceId
     * @param size
     */
    public static void add(String jobInstanceId, int size) {

        lineCountMap.get(jobInstanceId).addAndGet(size);
    }

    public static Long getProgressCount(String jobInstanceId) {

        return lineCountMap.get(jobInstanceId).get();
    }

}
