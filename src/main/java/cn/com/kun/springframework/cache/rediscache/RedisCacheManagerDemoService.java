package cn.com.kun.springframework.cache.rediscache;

import cn.com.kun.common.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
public class RedisCacheManagerDemoService {

    public final static Logger logger = LoggerFactory.getLogger(RedisCacheManagerDemoService.class);



    /**
     * 更新缓存
     *
     * value表示缓存器的名字，通过配置定义，初始化时定义
     * key表示放入缓存器时的key关键字，尽量唯一和非空，非空会报错
     *
     * @param req
     * @return
     */
    @CachePut(value = "systemParamCaffeineCache", key = "#req.message")
    public ResultVo testPut(ResultVo req){

        //这里会做具体的业务更新，例如更新数据库，更新redis之类的
        logger.info("进入cn.com.kun.springframework.caffeinecache.CaffeineCacheDemoService.test1");
        return ResultVo.valueOfSuccess("test1");
    }

    /**
     * 第一次访问时没走缓存，第二次开始就走缓存了
     * @param req
     * @return
     */
    @Cacheable(value = "systemParamCaffeineCache", key = "#req.message", sync = true)
    public ResultVo testQuery(ResultVo req){

        //这里会做具体的业务更新，例如更新数据库，更新redis之类的
        logger.info("进入cn.com.kun.springframework.caffeinecache.CaffeineCacheDemoService.testQuery");
        return ResultVo.valueOfSuccess("testQuery");
    }


}
