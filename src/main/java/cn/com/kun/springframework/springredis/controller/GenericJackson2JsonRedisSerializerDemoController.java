package cn.com.kun.springframework.springredis.controller;

import cn.com.kun.springframework.springredis.vo.RedisCacheVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * GenericJackson2JsonRedisSerializer的坑
 * 虽然RedisCacheVO属性相同，但是路径不同，会反序列化失败
 *
 * author:xuyaokun_kzx
 * date:2022/6/14
 * desc:
*/
@RequestMapping("/spring-redis-dateCountControl")
@RestController
public class GenericJackson2JsonRedisSerializerDemoController {

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 在kunwebdemo工程放入一个 RedisCacheVO
     * 在本工程进行反序列化，会报错
     * @return
     */
    @GetMapping(value = "/get")
    public String get(){

        //直接报错
        RedisCacheVO redisCacheVO = (RedisCacheVO) redisTemplate.opsForValue().get("redis-serializer");
        return "OK";
    }


}
