package cn.com.kun.springframework.springredis.controller;

import cn.com.kun.springframework.springredis.scan.RedisScanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequestMapping("/spring-redis-scan")
@RestController
public class RedisScanDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisScanDemoController.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired(required = false)
    JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    ApplicationContext applicationContext;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(HttpServletRequest request){

        String keyPrefix = "C1:";
        for (int i = 0; i < 100 * 10000; i++) {

            String key = UUID.randomUUID().toString();
            String value = UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(keyPrefix + key, value, 60, TimeUnit.DAYS);
        }

        return "OK";
    }

    @RequestMapping(value = "/scan-count", method = RequestMethod.GET)
    public String scanCount(HttpServletRequest request){

//        try {
//            Object obj = applicationContext.getBean("mySingleJedisConnectionFactory");
//            Object obj2 = applicationContext.getBean("jedisConnectionFactory2");
//        }catch (Exception e){
//
//        }


//        Long res = RedisClusterScanUtils.getCount("C1*", redisTemplate);
        Long res = RedisScanUtils.getCount("C1*", redisTemplate);
        LOGGER.info("{}", res);
        return "OK";
    }

    @RequestMapping(value = "/delete-all", method = RequestMethod.GET)
    public String deleteAll(HttpServletRequest request){

//        RedisClusterScanUtils.remove("C1*", redisTemplate);
        RedisScanUtils.remove("C1*", redisTemplate);
        return "OK";
    }
}
