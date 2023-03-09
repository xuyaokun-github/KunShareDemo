package cn.com.kun.component.memorycache.apply;

import cn.com.kun.component.memorycache.properties.MemoryCacheProperties;
import cn.com.kun.component.memorycache.redisImpl.MemoryCacheNoticeRedisVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    /**
     * 强依赖Redis
     */
    @Autowired
    private MemoryCacheNoticeRedisVisitor memoryCacheNoticeRedisVisitor;

    @Autowired
    private MemoryCacheProperties memoryCacheProperties;

    @Autowired
    private MemoryCacheCleaner memoryCacheCleaner;

    private Map<String, String> timeMillisMap = new HashMap<>();

    /**
     * 默认的检测间隔睡眠时间，1秒
     */
    private long sleepTime = 1000L;

    /**
     * 记录心跳次数
     */
    private int heartBeatCount = 0;


    @PostConstruct
    public void init(){

        if (memoryCacheProperties.isEnabled() && memoryCacheProperties.isApplyApp()){
            if (memoryCacheProperties.getApply().getDetectThreadSleepTime() > 0){
                sleepTime = memoryCacheProperties.getApply().getDetectThreadSleepTime();
            }
            new Thread(()->{
                doCheck();
            }, "MemoryCacheRedisDetector-Thread").start();
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
        if (heartBeatCount == 10){
            LOGGER.info("MemoryCacheRedisDetector working...");
            heartBeatCount = 0;
        }
    }

    private void check() {
        /**
         * 获取整个hash结构
         */
        Map<Object, Object> redisMap = memoryCacheNoticeRedisVisitor.getHash(NOTICE_TIMEMILLIS_HASH_KEYNAME);
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
                    memoryCacheCleaner.clearCache(configName);
                    updateTimemillis(configName, lastUpdateTime);
                    LOGGER.info("本次清空缓存管理器{},更新时间戳为：{}", configName, lastUpdateTime);
                } else {
                    //未发生变更
                    if (LOGGER.isDebugEnabled()){
                        LOGGER.debug("缓存管理器{}未发生变更", configName);
                    }
                }
            }
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
