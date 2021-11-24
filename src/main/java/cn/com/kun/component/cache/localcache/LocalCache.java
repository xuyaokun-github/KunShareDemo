package cn.com.kun.component.cache.localcache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * 本地缓存抽象
 *
 * author:xuyaokun_kzx
 * date:2021/11/24
 * desc:
*/
public abstract class LocalCache<T> {

    private Map<String, Map<String, T>> targetCache = new ConcurrentHashMap<>();

    protected T get(String cacheName, String timeStamp, Supplier<T> supplier) {

        T target = null;

        Map<String, T> timeStampMap = targetCache.get(cacheName);
        if (timeStampMap == null){
            timeStampMap = new ConcurrentHashMap();
            targetCache.put(cacheName, timeStampMap);
        }

        target = timeStampMap.get(timeStamp);
        if (target == null){
            //假如使用最新的时间戳获取不到，说明模板发生了变化，必须将旧缓存清空
            timeStampMap.clear();
            //创建T
            target = supplier.get();
            timeStampMap.put(timeStamp, target);
        }
        return target;
    }

}
