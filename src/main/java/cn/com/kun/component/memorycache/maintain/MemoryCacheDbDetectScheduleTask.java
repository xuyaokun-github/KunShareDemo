package cn.com.kun.component.memorycache.maintain;

import cn.com.kun.component.memorycache.properties.MemoryCacheProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 针对多套redis的双活架构，检测数据库是否有最新的数据
 * 假如有则做通知
 * author:xuyaokun_kzx
 * date:2021/7/7
 * desc:
*/
@EnableScheduling
@Component
public class MemoryCacheDbDetectScheduleTask {

    private final static Logger LOGGER = LoggerFactory.getLogger(MemoryCacheDbDetectScheduleTask.class);

    @Autowired
    private MemoryCacheDBDetector memoryCacheDBDetector;

    @Autowired
    private MemoryCacheProperties memoryCacheProperties;

    /**
     * 六十秒检测一次
     */
    @Scheduled(fixedRate = 60000L)
    public void detect(){

        if (!memoryCacheProperties.isEnabled() || !memoryCacheProperties.isMaintainApp()
                || !memoryCacheProperties.getMaintain().isMultiRedis()
                || StringUtils.isEmpty(memoryCacheProperties.getMaintain().getClusterName())){
            return;
        }
        LOGGER.info("MemoryCacheDBDetector running");
        memoryCacheDBDetector.checkDb();
    }

}
