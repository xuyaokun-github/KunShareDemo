package cn.com.kun.kafka.dataStatMonitor.stat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * author:xuyaokun_kzx
 * date:2023/8/18
 * desc:
 * 注意一个问题，Pod1 可能没生产，但是可能会消费，所以执行减1时可能遇到负数
*/
public class TopicDataStatProcessor {

    /**
     * 可能会有多个主题
     */
    private static final Map<String, Map<String, AtomicLong>> msgTypeCountMap = new ConcurrentHashMap<>();

    /**
     * 计数加1
     * TODO 考虑线程安全优化
     *
     * @param topic
     * @param msgType
     */
    public synchronized static void add(String topic, String msgType) {

        Map<String, AtomicLong> countMap = msgTypeCountMap.get(topic);
        if (countMap == null){
            countMap = new ConcurrentHashMap<>();
            msgTypeCountMap.put(topic, countMap);
            countMap.put(msgType, new AtomicLong(1));
        }else {
            AtomicLong count = countMap.get(msgType);
            if (count == null){
                countMap.put(msgType, new AtomicLong(1));
            }else {
                count.incrementAndGet();
            }
        }

    }

    /**
     * 计数减一
     *
     * @param topic
     * @param msgType
     */
    public synchronized static void subtract(String topic, String msgType) {

        Map<String, AtomicLong> countMap = msgTypeCountMap.get(topic);
        if (countMap == null){
            countMap = new ConcurrentHashMap<>();
            msgTypeCountMap.put(topic, countMap);
            countMap.put(msgType, new AtomicLong(-1));
        }else {
            AtomicLong count = countMap.get(msgType);
            if (count == null){
                countMap.put(msgType, new AtomicLong(-1));
            }else {
                count.decrementAndGet();
            }
        }

    }

    /**
     * 当监控到主题无堆积时，可清空整个数据（以防漏加漏减）
     * @param topic
     */
    public static void reset(String topic){

        Map<String, AtomicLong> countMap = msgTypeCountMap.get(topic);
        countMap.clear();

    }


}
