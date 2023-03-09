package cn.com.kun.component.memorycache.apply;

import cn.com.kun.component.memorycache.properties.MemoryCacheProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

/**
 * 缓存清理器
 *
 * author:xuyaokun_kzx
 * date:2023/1/19
 * desc:
*/
@Component
public class MemoryCacheCleaner {

    @Autowired
    private MemoryCacheProperties memoryCacheProperties;

    @Autowired
    private ApplicationContext applicationContext;

    private CacheManager cacheManager;

    @PostConstruct
    public void init(){

        if (memoryCacheProperties.isEnabled() && memoryCacheProperties.isApplyApp()){
            initCacheManager();
        }
    }


    //org.springframework.cache.caffeine.CaffeineCacheManager
    private void initCacheManager() {

        String caffeineCacheManagerName = memoryCacheProperties.getApply().getCaffeineCacheManagerName();
        if (StringUtils.isEmpty(caffeineCacheManagerName)){
            cacheManager = applicationContext.getBean(SimpleCacheManager.class);
        }else {
            cacheManager = (CacheManager) applicationContext.getBean(caffeineCacheManagerName);
        }
        Assert.notNull(cacheManager, "内存管理器cacheManager不能为空");
    }

    public void clearCache(String configName) {

        if(cacheManager != null){
            cacheManager.getCache(configName).clear();
        }
    }

    public void clearCache(String configName, String bizKey) {
        if(cacheManager != null) {
            if (StringUtils.isNotEmpty(bizKey)){
                cacheManager.getCache(configName).evict(bizKey);
            }else {
                //假如Key为空，全部清除（针对某些场景，可能缓存key拼接了其他业务字段，不推荐这么用）
                cacheManager.getCache(configName).clear();
            }
        }
    }


    public CacheManager getCacheManager() {

        return cacheManager;
    }

}
