package cn.com.kun.springframework.cache.rediscache;

import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RequestMapping("/rediscachemanager")
@RestController
public class RedisCacheManagerDemoController {

    public final static Logger LOGGER = LoggerFactory.getLogger(RedisCacheManagerDemoController.class);

    @Autowired
    private RedisCacheManagerDemoService redisCacheManagerDemoService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 拿到缓存管理器，它可以操作所有缓存对象
     * （这里拿到是哪种实现呢？基于redis的，还是基于内存的？）
     *
     */
    @Autowired
    @Qualifier("redisCacheManager")
    private CacheManager cacheManager;

    @RequestMapping("/testPut")
    public ResultVo testPut(){

        ResultVo res = redisCacheManagerDemoService.testPut(ResultVo.valueOfError("testPut", "testPut"));
        return res;
    }

    @RequestMapping("/testQuery")
    public ResultVo testQuery(){

        ResultVo res = redisCacheManagerDemoService.testQuery(ResultVo.valueOfError("testPut", "testPut"));
        return res;
    }

    @RequestMapping("/showCacheManager")
    public ResultVo showCacheManager(){


        Object resultVo = redisTemplate.opsForValue().get("RedisCacheManager-Default-testPut");
        Collection<String> cacheNames = cacheManager.getCacheNames();
        RedisCache cache = (RedisCache) cacheManager.getCache("");
        Cache cache1 = null;
        Cache.ValueWrapper valueWrapper = cache.get("RedisCacheManager-Default-testPut");
        Cache.ValueWrapper valueWrapper1 = cache.get("testPut");
        resultVo = valueWrapper1.get();
        return ResultVo.valueOfSuccess();
    }

    @RequestMapping("/getValueFromRedisCache")
    public ResultVo getValueFromRedisCache(){

        //下面这种方式是获取不到的，RedisCacheManager-Default-testPut是redis服务端看到的key名
        Object resultVo = redisTemplate.opsForValue().get("RedisCacheManager-Default-testPut");
        //获取所有缓存容器的名称
        Collection<String> cacheNames = cacheManager.getCacheNames();
        //获取默认的缓存容器
        RedisCache cache = (RedisCache) cacheManager.getCache("");
        //取值（错误的写法，不能用整个key名，因为spring还有一个拼前缀的过程，假如自己也拼了，算是重复了）
        Cache.ValueWrapper valueWrapper = cache.get("RedisCacheManager-Default-testPut");
        //取值（正确的写法）
        Cache.ValueWrapper valueWrapper1 = cache.get("testPut");
        //获取具体的对象
        resultVo = valueWrapper1.get();
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

    /**
     * 框架暂时没有提供api 动态添加缓存器
     * 所以最佳实践还是在初始化时通过setCaches添加进去
     * @return
     */
    @RequestMapping("/addCacheObj")
    public ResultVo addCacheObj(){

        /**
         * 假如非要做到动态添加，也是可以的，
         * 第一种方法通过反射调用org.springframework.cache.support.SimpleCacheManager#loadCaches()方法
         * 第二种方法就是先把ArrayList<CaffeineCache> caches缓存下来，后续要动态添加缓存器时直接替换整个caches
         */
        ((SimpleCacheManager) cacheManager).getCacheNames();
        return ResultVo.valueOfSuccess();
    }

}
