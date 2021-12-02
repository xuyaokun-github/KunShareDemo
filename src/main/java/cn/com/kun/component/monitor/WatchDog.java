package cn.com.kun.component.monitor;

import java.util.HashMap;
import java.util.Map;

/**
 * 作为主线程和监控线程之间的通信载体
 * 通过变量的方式停业务线程
 *
 * author:xuyaokun_kzx
 * date:2021/11/26
 * desc:
*/
public class WatchDog {

    private static Map<String, String> flagMap = new HashMap<>();

    public static void addFlag(String jobId, String flag) {
        flagMap.put(jobId, flag);
    }

    public static String hasFlag(String jobId) {
        return flagMap.get(jobId);
    }

    /**
     * 任务成功执行完之后，清楚标志
     * 表示已经退出运行
     * @param jobId
     */
    public static void removeFlag(String jobId) {
        flagMap.remove(jobId);
    }
}
