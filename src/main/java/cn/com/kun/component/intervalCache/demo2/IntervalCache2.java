package cn.com.kun.component.intervalCache.demo2;


import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.component.intervalCache.demo1.IntervalCacheDataLoader;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.com.kun.common.utils.DateUtils.PATTERN_YYYYMMDD;
import static cn.com.kun.common.utils.DateUtils.PATTERN_YYYYMMDDHHMMSS;

/**
 * 实现2--基于LRU
 * 亲测成功
 *
 * author:xuyaokun_kzx
 * date:2022/4/1
 * desc:
 */
public class IntervalCache2 {

    /**
     *  Map<String, Object> map = Collections.synchronizedMap(new LinkedHashMap<>());
     */
    private MyIntervalCacheLRUCache<String, Object> myIntervalCacheLRUCache = new MyIntervalCacheLRUCache(1);

    private IntervalCacheDataLoader dataLoader;

    private TimeUnit timeUnit = TimeUnit.HOURS;

    private Long num = 1L;

    private String baseDateString = "20220101";

    public IntervalCache2(IntervalCacheDataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public IntervalCache2(TimeUnit timeUnit, Long num, IntervalCacheDataLoader dataLoader) {
        if (!timeUnit.equals(TimeUnit.HOURS) && !timeUnit.equals(TimeUnit.DAYS)
                && !timeUnit.equals(TimeUnit.MINUTES) && !timeUnit.equals(TimeUnit.SECONDS)){
            //功能不支持
            throw new RuntimeException("时间单位不支持");
        }
        this.dataLoader = dataLoader;
        this.timeUnit = timeUnit;
        this.num = num;
    }

    public <T> void set(T obj) {
        String key = buildKey();
        System.out.println(Thread.currentThread().getName() + "执行set, key:" + key);
        myIntervalCacheLRUCache.put(key, obj);
    }

    /**
     * 检查间隔时间是否到了
     * 假如时间间隔到了，调用获取数据方法，重新设置
     *
     * @return
     */
    public boolean check() {

        Object obj = getOnlyByKey();
        if (myIntervalCacheLRUCache.isEmpty() || obj == null) {
            //超过间隔了，可以重新检测了
            Object newData = load();
            if (newData != null){
                set(newData);
            }else {
                //取旧值，然后填进来
                //TODO
//                set();
            }
            System.out.println(Thread.currentThread().getName() + "重新设值完毕");
            return true;
        } else {
            //无需更新，继续用旧值

        }
        return false;
    }

    private <T> T load() {

        return dataLoader.loadData();
    }

    public <T> T getOnlyByKey() {
        String key = buildKey();
//        System.out.println(Thread.currentThread().getName() + "尝试获取, key:" + key);
        T res = (T) myIntervalCacheLRUCache.get(key);
        return res;
    }

    public <T> T get() {
        String key = buildKey();
//        System.out.println(Thread.currentThread().getName() + "尝试获取, key:" + key);
        T res = (T) myIntervalCacheLRUCache.get(key);
        if (res == null){
            res = getOldVal();
        }
        return res;
    }

    private <T> T getOldVal() {
        if (myIntervalCacheLRUCache.size() > 0) {
            for (Map.Entry entry : myIntervalCacheLRUCache.entrySet()) {
                if (entry.getValue() != null){
                    return (T) entry.getValue();
                }
            }
        }
        return null;
    }


    private String buildKey() {

        String key = "";
        Date currentTime = new Date();
        //和baseDate做比较
        if (timeUnit.equals(TimeUnit.DAYS)){
            //算出相距的天数，然后算出余数，用当前时间减去余数的天数，然后获取该天的字符串
            long number = DateUtils.betweenDays(baseDateString, DateUtils.toStr(currentTime, PATTERN_YYYYMMDD)) % num;
            Date date = DateUtils.addDays(currentTime, number);
            key = DateUtils.toStr(date, "yyyyMMdd");
        }else if (timeUnit.equals(TimeUnit.HOURS)){
            //TODO

        }else if (timeUnit.equals(TimeUnit.MINUTES)){
            //TODO

        }else if (timeUnit.equals(TimeUnit.SECONDS)){
            long number = DateUtils.betweenSeconds(baseDateString + "000000", DateUtils.toStr(currentTime, PATTERN_YYYYMMDDHHMMSS)) % num;
            Date date = DateUtils.addSeconds(currentTime, 0 - number);
            key = DateUtils.toStr(date, "yyyyMMddHHmmss");
        }
        return key;
    }

}
