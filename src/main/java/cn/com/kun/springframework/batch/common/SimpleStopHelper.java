package cn.com.kun.springframework.batch.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简易版的停止job辅助类
 *
 * Created by xuyaokun On 2022/6/19 12:17
 * @desc:
 */
public class SimpleStopHelper {

    /**
     * 假如为true，表示需要停止该Job
     */
    private static Map<String, Boolean> jobStopFlagMap = new ConcurrentHashMap<>();

    public static boolean isNeedStop(String jobName) {

        return jobStopFlagMap.get(jobName) != null && jobStopFlagMap.get(jobName).booleanValue();
    }


    public static void removeStopFlag(String jobName) {

        jobStopFlagMap.remove(jobName);
    }

    public static void markStop(String jobName) {
        jobStopFlagMap.put(jobName, true);
    }
}
