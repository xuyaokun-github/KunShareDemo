package cn.com.kun.component.memorycache.apply;

import cn.com.kun.component.memorycache.properties.MemoryCacheProperties;
import cn.com.kun.component.redis.RedisTemplateHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static cn.com.kun.component.memorycache.constants.MemoryCacheConstants.NOTICE_TIMEMILLIS_HASH_KEYNAME;

/**
 * 内存缓存检测处理器
 * 作用：检测是否有缓存器等待刷新，通过时间戳判断
 *
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
@Component
public class MemoryCacheRedisDetector {

    private final static Logger LOGGER = LoggerFactory.getLogger(MemoryCacheRedisDetector.class);

    private CacheManager cacheManager;

    @Autowired
    private RedisTemplateHelper redisTemplateHelper;

    @Autowired
    private ApplicationContext applicationContext;

    private long sleepTime = 1000L;

    private int heartBeatCount = 0;

    private Map<String, String> timeMillisMap = new HashMap<>();

    @Autowired
    private MemoryCacheProperties memoryCacheProperties;

    @PostConstruct
    public void init(){

        //org.springframework.cache.caffeine.CaffeineCacheManager
        initCacheManager();
        sleepTime = memoryCacheProperties.getDetectThreadSleepTime();
        new Thread(()->{
            doCheck();
        }, "MemoryCacheRedisDetector-Thread").start();
    }

    private void initCacheManager() {

        if (StringUtils.isEmpty(memoryCacheProperties.getCaffeineCacheManagerName())){
            cacheManager = applicationContext.getBean(SimpleCacheManager.class);
        }else {
            cacheManager = (CacheManager) applicationContext.getBean(memoryCacheProperties.getCaffeineCacheManagerName());
        }
    }

    private void doCheck() {

        while (memoryCacheProperties.isEnabled()){
            try {
                check();
                Thread.sleep(sleepTime);
                logHeartBeat();
            } catch (Exception e) {
                LOGGER.error("MemoryCacheRedisDetector doCheck方法出现异常", e);
            }
        }

    }

    /**
     * 记录心跳日志
     */
    private void logHeartBeat() {
        heartBeatCount++;
        if (heartBeatCount == 5){
            LOGGER.info("MemoryCacheRedisDetector working...");
            heartBeatCount = 0;
        }
    }

    private void check() {
        /**
         * 获取整个hash结构
         */
        Map<Object,Object> redisMap = redisTemplateHelper.hmget(NOTICE_TIMEMILLIS_HASH_KEYNAME);
        Iterator<Map.Entry<Object, Object>> iterator = redisMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Object, Object> entry = iterator.next();
            String configName = (String) entry.getKey();
            String lastUpdateTime = (String) entry.getValue();
            String oldUpdateTime = timeMillisMap.get(configName);
            if (oldUpdateTime == null){
                timeMillisMap.put(configName, lastUpdateTime);
            }else {
                if (!oldUpdateTime.equals(lastUpdateTime)){
                    //redis中的时间戳和timeMillisMap中的时间不等，说明发生变更
                    //清缓存
                    clearCache(configName, lastUpdateTime);
                } else {
                    //未发生变更
//                    LOGGER.debug("缓存管理器{}未发生变更", configName);
                }
            }
        }
    }

    private void clearCache(String configName, String lastUpdateTime) {

        if(cacheManager != null){
            cacheManager.getCache(configName).clear();
            updateTimemillis(configName, lastUpdateTime);
            LOGGER.info("本次清空缓存管理器{},更新时间戳为：{}", configName, lastUpdateTime);
        }
    }

    /**
     * 更新某个缓存管理器的时间戳
     * @param configName
     * @param lastUpdateTime
     */
    public void updateTimemillis(String configName, String lastUpdateTime){
        timeMillisMap.put(configName, lastUpdateTime);
    }
}
