package cn.com.kun.springframework.springredis.controller;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springredis.counter.RedisCounter;
import cn.com.kun.springframework.springredis.counter.RedisCounterDemoService;
import cn.com.kun.springframework.springredis.counter.RedisMaxValueIncrementer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static cn.com.kun.common.utils.DateUtils.PATTERN_YYYY_MM_DD;

/**
 * author:xuyaokun_kzx
 * date:2022/7/12
 * desc:
*/
@RequestMapping("/spring-redis-count")
@RestController
public class SpringRedisCountDemocontroller {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringRedisCountDemocontroller.class);

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    private RedisCounterDemoService redisCounterDemoService;

    @Autowired
    private RedisCounter redisCounter;

    @Autowired
    private RedisMaxValueIncrementer redisMaxValueIncrementer;

    @GetMapping(value = "/testRedisCounter")
    public ResultVo testRedisCounter(HttpServletRequest request){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                redisCounterDemoService.addRequest();
            }).start();
        }

        return ResultVo.valueOfSuccess("");
    }

    /**
     * 计数器很快就过期的场景
     * @param request
     * @return
     */
    @GetMapping(value = "/testRedisCounter2")
    public ResultVo testRedisCounter2(HttpServletRequest request){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                //一个key就对应一个计数器
                String key = "redisCounter-msg-send2-" + DateUtils.toStr(new Date(), PATTERN_YYYY_MM_DD);
                long res = redisCounter.add(key, 1, 20, TimeUnit.SECONDS);
                LOGGER.info("redisCounter加一后：{}", res);
            }).start();
        }

        return ResultVo.valueOfSuccess("");
    }

    @GetMapping(value = "/testRedisMaxValueIncrementer")
    public ResultVo testRedisMaxValueIncrementer(){

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                //一个key就对应一个计数器
                String key = "redisCounter-msg-send2-" + DateUtils.toStr(new Date(), PATTERN_YYYY_MM_DD);
                long res = redisMaxValueIncrementer.nextLongValue(key, 1, TimeUnit.DAYS);
                LOGGER.info("redisMaxValueIncrementer加一后：{}", res);
            }).start();
        }

        return ResultVo.valueOfSuccess("");
    }

}
