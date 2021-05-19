package cn.com.kun.springframework.cache.caffeinecache;

import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/caffeinecache")
@RestController
public class CaffeineCacheDemoController {

    public final static Logger LOGGER = LoggerFactory.getLogger(CaffeineCacheDemoController.class);

    @Autowired
    private CaffeineCacheDemoService caffeineCacheDemoService;



    /**
     * 拿到缓存管理器，它可以操作所有缓存对象
     */
    @Autowired
    @Qualifier("caffeineCacheManager")
    private CacheManager cacheManager;

    @RequestMapping("/testPut")
    public ResultVo testPut(){

        ResultVo res = caffeineCacheDemoService.test1(ResultVo.valueOfError("testPut", "testPut"));
        return res;
    }

    @RequestMapping("/testQuery")
    public ResultVo testQuery(){

        ResultVo res = caffeineCacheDemoService.testQuery(ResultVo.valueOfError("testPut", "testPut"));
        return res;
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
