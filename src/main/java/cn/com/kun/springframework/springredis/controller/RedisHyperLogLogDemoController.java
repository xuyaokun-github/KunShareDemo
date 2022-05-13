package cn.com.kun.springframework.springredis.controller;

import cn.com.kun.springframework.springredis.service.RedisHyperLogLogDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * N天不能超过M次需求
 *
 * Created by xuyaokun On 2022/1/14 13:42
 * @desc:
 */
@RequestMapping("/spring-redis-hyperLogLog")
@RestController
public class RedisHyperLogLogDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisHyperLogLogDemoController.class);

    @Autowired
    RedisHyperLogLogDemoService redisHyperLogLogDemoService;

    /***
     * @return
     */
    @GetMapping(value = "/test1")
    public String test1(){

        redisHyperLogLogDemoService.delete("kunghsu-hll");
        for (int i = 0; i < 10000; i++) {
            redisHyperLogLogDemoService.add("kunghsu-hll", UUID.randomUUID().toString());
        }
        LOGGER.info("HyperLogLog总数：{}", redisHyperLogLogDemoService.size("kunghsu-hll"));
        return "OK";
    }

}
