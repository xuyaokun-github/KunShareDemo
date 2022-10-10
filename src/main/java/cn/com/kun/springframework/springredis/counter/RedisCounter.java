package cn.com.kun.springframework.springredis.counter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis计数器
 * author:xuyaokun_kzx
 * date:2022/7/12
 * desc:
*/
@Component
public class RedisCounter {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 因为操作具有原子性，所以这方法是线程安全的，不会少加漏加
     * @param key
     * @param num
     * @param time
     * @param unit
     * @return
     */
    public Long add(String key, long num, long time, TimeUnit unit){

        //加一的时候没法同时设置过期时间,
        redisTemplate.expire(key, time, unit);
        //假如该key对应的计数器不存在，就会默认从0开始加
        return redisTemplate.opsForValue().increment(key, num);
    }

    public Long get(String key){

        return (Long) redisTemplate.opsForValue().get(key);
    }
}
