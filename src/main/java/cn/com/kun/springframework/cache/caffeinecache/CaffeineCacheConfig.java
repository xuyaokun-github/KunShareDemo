package cn.com.kun.springframework.cache.caffeinecache;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
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
    public CacheManager caffeineCacheManager() {

        SimpleCacheManager cacheManager = new SimpleCacheManager();
        ArrayList<CaffeineCache> caches = Lists.newArrayList();
        List<CacheBean> list = setCacheBean();
        for (CacheBean cacheBean : list) {
            /**
             * 一个CaffeineCache就相当于一类业务，可以为不同类的业务建立不同的CaffeineCache
             * 假如添加了同名的CaffeineCache会怎样？
             * cacheManager并不会排重！
             * 假如放一个元素进CaffeineCache，会放两份吗？
             * 并不会，spring只会放一份，所以同名的两个CaffeineCache钟只有一个CaffeineCache里有数据
             */
            caches.add(new CaffeineCache(cacheBean.getKey(),
                    Caffeine.newBuilder().recordStats()
                            .expireAfterWrite(cacheBean.getTtl(), TimeUnit.SECONDS) //过期时间
                            .maximumSize(cacheBean.getMaximumSize()) //缓存最大的条目数
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
     * 初始化一些缓存的 key
     * * @return
     */
    private List<CacheBean> setCacheBean() {
        List<CacheBean> list = Lists.newArrayList();
        CacheBean userCache = new CacheBean();
        userCache.setKey("userCache");
        userCache.setTtl(60);
        userCache.setMaximumSize(10000);
        CacheBean deptCache = new CacheBean();
        deptCache.setKey("userCache2");
        deptCache.setTtl(60);
        deptCache.setMaximumSize(10000);
        list.add(userCache);
        list.add(deptCache);
        return list;
    }

    class CacheBean {

        private String key;
        private long ttl;
        private long maximumSize;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public long getTtl() {
            return ttl;
        }

        public void setTtl(long ttl) {
            this.ttl = ttl;
        }

        public long getMaximumSize() {
            return maximumSize;
        }

        public void setMaximumSize(long maximumSize) {
            this.maximumSize = maximumSize;
        }
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