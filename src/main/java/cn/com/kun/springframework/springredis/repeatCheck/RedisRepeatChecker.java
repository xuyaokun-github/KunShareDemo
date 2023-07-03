package cn.com.kun.springframework.springredis.repeatCheck;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Created by xuyaokun On 2022/11/12 1:13
 * @desc:
 */
@Component
public class RedisRepeatChecker {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 假如重复，返回false
     * 假如未重复，返回true 说明校验通过
     * @return
     */
    public boolean checkAndSet(String key){

        //判断key值是否存在，存在则不存储，不存在则存储
        return checkAndSet(key, 1, TimeUnit.HOURS);
    }

    /**
     * 自定义过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public boolean checkAndSet(String key, long timeout, TimeUnit unit){

        return redisTemplate.opsForValue().setIfAbsent(key, "", timeout, unit);
    }


}
