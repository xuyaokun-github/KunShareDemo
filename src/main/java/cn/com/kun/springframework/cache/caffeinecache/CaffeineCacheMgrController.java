package cn.com.kun.springframework.cache.caffeinecache;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.utils.ReflectUtils;
import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;

@RequestMapping("/caffeinecache-mgr")
@RestController
public class CaffeineCacheMgrController {

    public final static Logger LOGGER = LoggerFactory.getLogger(CaffeineCacheMgrController.class);

    /**
     * 拿到缓存管理器，它可以操作所有缓存对象
     */
    @Autowired
    @Qualifier("caffeineCacheManager")
    private CacheManager cacheManager;

    /**
     * http://localhost:8080/kunsharedemo/caffeinecache-mgr/showAllCache?cacheName=memorycache-student-service
     * @param cacheName
     * @return
     */
    @RequestMapping("/showAllCache")
    public ResultVo showAllCache(String cacheName){

        Cache cache = cacheManager.getCache(cacheName);
        //缓存管理器没有提供api直接获取整个缓存对象
        Object nativeCache = cache.getNativeCache();
        /**
         * 假如非要获取整个缓存容器对象，只能用反射
         * 容器对象实质的类型是com.github.benmanes.caffeine.cache.Cache<Object, Object>
         */
        CaffeineCache caffeineCache = (CaffeineCache) cache;
        try {
            com.github.benmanes.caffeine.cache.Cache innerCache = (com.github.benmanes.caffeine.cache.Cache) ReflectUtils.getValue(caffeineCache, "cache");
            LOGGER.info("获取的类型：{}", innerCache);
            /**
             * com.github.benmanes.caffeine.cache.Cache提供了api直接获取整个容器对象
             * 容器其实就是一个ConcurrentMap.
             * com.github.benmanes.caffeine.cache.BoundedLocalCache#data
             * 封装得太深了，不是很方便直接查看
             */
            ConcurrentMap concurrentMap = innerCache.asMap();
            //到这一步已经获取到了整个对象（下面的方法不用关注了)
            LOGGER.info("整个缓存容器对象内容：{}", JacksonUtils.toJSONString(concurrentMap));
//            LoadingCache loadingCache = ((LoadingCache<Object, Object>) innerCache);

            Object obj = ReflectUtils.getValue(innerCache, "cache");
            LOGGER.info("obj：{}", obj);


            Object obj2 = ReflectUtils.getValueIncludeParent(obj, "data");

            LOGGER.info("obj2：{}", obj2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultVo.valueOfSuccess();
    }

    /**
     * http://localhost:8080/kunsharedemo/caffeinecache-mgr/showOneKey?cacheName=memorycache-student-service&key=10
     * @param cacheName
     * @param key
     * @return
     */
    @RequestMapping("/showOneKey")
    public ResultVo showOneKey(String cacheName, String key){

        Cache cache = cacheManager.getCache(cacheName);
        Cache.ValueWrapper valueWrapper = cache.get(key);
        Object value = valueWrapper == null? null:valueWrapper.get();
        return ResultVo.valueOfSuccess(value);
    }

    @RequestMapping("/showCacheManager")
    public ResultVo showCacheManager(){

        cacheManager.getCacheNames();
        return ResultVo.valueOfSuccess();
    }

    @RequestMapping("/manualDelete")
    public ResultVo manualDelete(){

        /*
            手动清除某个业务类的缓存器
            当在分布式环境下通过某个pod更新缓存时，务必通知所有节点清除缓存！
            可以用clear方法进行缓存清除
         */
        cacheManager.getCache("systemParamCaffeineCache").clear();
        return ResultVo.valueOfSuccess();
    }


}
