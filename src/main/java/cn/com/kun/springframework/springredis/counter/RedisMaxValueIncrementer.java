package cn.com.kun.springframework.springredis.counter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis序号增量器
 * author:xuyaokun_kzx
 * date:2022/7/12
 * desc:
*/
@Component
public class RedisMaxValueIncrementer {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 因为操作具有原子性，所以这方法是线程安全的，不会少加漏加
     * @param key 由业务使用方决定
     * @param time
     * @param unit
     * @return
     */
    public Long nextLongValue(String key, long time, TimeUnit unit){

        if (time >= 1){
            //加一的时候没法同时设置过期时间
            redisTemplate.expire(key, time, unit);
        }
        //假如该key对应的计数器不存在，就会默认从0开始加
        return redisTemplate.opsForValue().increment(key, 1);
    }

    public Long nextLongValue(String key){

        //假如该key对应的计数器不存在，就会默认从0开始加
        return redisTemplate.opsForValue().increment(key, 1);
    }

}
