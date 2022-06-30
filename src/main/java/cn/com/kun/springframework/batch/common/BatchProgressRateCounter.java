package cn.com.kun.springframework.batch.common;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 批处理进度统计器
 *
 * Created by xuyaokun On 2022/6/18 13:51
 * @desc:
 */
public class BatchProgressRateCounter {

    private static ThreadLocal<AtomicLong> lineCountThreadLocal = new ThreadLocal<>();


    public static void initCounter() {

        lineCountThreadLocal.set(new AtomicLong(0));
    }


    public static void removeCounter() {

        lineCountThreadLocal.remove();
    }


    public static void add(int size) {

        lineCountThreadLocal.get().addAndGet(size);
    }

    public static Long getProgressCount() {

        return lineCountThreadLocal.get().get();
    }
}
