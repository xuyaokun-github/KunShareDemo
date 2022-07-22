package cn.com.kun.config.cache;

import cn.com.kun.common.enums.MemoryCacheConfigurantionsEnum;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuyaokun On 2021/5/13 22:51
 *
 * @desc:
 */
@Configuration
public class CaffeineCacheConfig {

    /**
     * 创建基于Caffeine的Cache Manager
     * 初始化一些key存入
     *
     * @return
     */
    @Bean
//    @Primary
    // Primary不是必须的，可用可不用，不用的时候就要用@Qualifier
    public SimpleCacheManager caffeineCacheManager() {

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        //缓存管理器集合
        ArrayList<CaffeineCache> caches = Lists.newArrayList();

        //遍历枚举类
        for(MemoryCacheConfigurantionsEnum configurantionsEnum : MemoryCacheConfigurantionsEnum.values()) {
            /**
             * 一个CaffeineCache就相当于一类业务，可以为不同类的业务建立不同的CaffeineCache
             * 假如添加了同名的CaffeineCache会怎样？
             * cacheManager并不会排重！
             * 假如放一个业务数据进CaffeineCache，会放两份吗（因为管理器存在重复的）？
             * 并不会，spring只会放一份管理器里，所以同名的两个CaffeineCache中只有一个CaffeineCache里有数据
             */
            caches.add(new CaffeineCache(configurantionsEnum.getConfigName(),
                    Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(configurantionsEnum.getTtl(), TimeUnit.SECONDS) //过期时间
                            .maximumSize(configurantionsEnum.getMaximumSize()) //缓存最大的条目数
                            .build()));
        }

        /*
            这里还可以继续添加其他业务类的缓存器，例如商品类、系统参数类
            可以通过bean的方式定义
         */
        caches.add(systemParamCaffeineCache());

        cacheManager.setCaches(caches);
        return cacheManager;
    }

    @Bean
    CaffeineCache systemParamCaffeineCache(){
        return new CaffeineCache("systemParamCaffeineCache",
                Caffeine.newBuilder().recordStats()
                        .expireAfterWrite(120, TimeUnit.SECONDS) //过期时间
                        .maximumSize(10000) //缓存最大的条目数
                        .build());
    }

    /**
     * 相当于在构建LoadingCache对象的时候 build()方法中指定过期之后的加载策略方法
     * * 必须要指定这个Bean，refreshAfterWrite=60s属性才生效
     * * @return
     */
//    @Bean
    public CacheLoader<String, Object> cacheLoader() {

        CacheLoader<String, Object> cacheLoader = new CacheLoader<String, Object>() {
            @Override
            public Object load(String key) throws Exception {
                return null;
            }

            // 重写这个方法将oldValue值返回回去，进而刷新缓存
            @Override
            public Object reload(String key, Object oldValue) throws Exception {
                return oldValue;
            }
        };
        return cacheLoader;
    }


}