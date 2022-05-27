package cn.com.kun.springframework.springredis.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * Created by xuyaokun On 2020/10/14 22:56
 * @desc: 
 */
@RequestMapping("/spring-redis-expire")
@RestController
public class SpringRedisExpireDemocontroller {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringRedisExpireDemocontroller.class);

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * @param request
     * @return
     */
    @RequestMapping(value = "/testExpire", method = RequestMethod.GET)
    public String testExpire(HttpServletRequest request){

        redisTemplate.opsForValue().set("20220519-key", "", 10, TimeUnit.SECONDS);
        redisTemplate.expire("2022*-key", 10000, TimeUnit.SECONDS);

        redisTemplate.expire("20225000022-key", 10000, TimeUnit.SECONDS);
        return "OK";
    }


}
