package cn.com.kun.springframework.springredis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 实现黑名单的功能-用什么数据结构，视需求决定
 *
 * author:xuyaokun_kzx
 * date:2021/6/23
 * desc:
*/
@Service
public class RedisHyperLogLogDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RedisHyperLogLogDemoService.class);

    @Autowired
    private RedisTemplate redisTemplate;

    public void delete(String key){
        redisTemplate.delete(key);
    }

    /**
     *
     * @param
     */
    public void add(String key, String value){

        redisTemplate.opsForHyperLogLog().add(key, value);
    }

    public Long size(String key){

        return redisTemplate.opsForHyperLogLog().size(key);
    }

}
