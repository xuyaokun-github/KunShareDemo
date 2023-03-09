package cn.com.kun.component.memorycache.redisImpl;

import cn.com.kun.component.memorycache.vo.MemoryCacheNoticeMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class DefaultMemoryCacheNoticeRedisVisitor implements MemoryCacheNoticeRedisVisitor{

    /**
     * 使用方有两个选择：
     * 1.定义一个RedisTemplate
     * 2.不定义RedisTemplate，自定义MemoryCacheNoticeRedisVisitor，覆盖DefaultMemoryCacheNoticeRedisVisitor
     */
    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void init(){
        Assert.notNull(redisTemplate, "MemoryCache组件DefaultMemoryCacheNoticeRedisVisitor依赖redisTemplate不能为空");
    }

    @Override
    public Map<Object, Object> getHash(String keyName) {

        Map<Object,Object> redisMap = redisTemplate.opsForHash().entries(keyName);
        return redisMap;
    }


    @Override
    public void put(String keyName, String hashKeyName, String value) {
        redisTemplate.opsForHash().put(keyName, hashKeyName, value);
    }


    @Override
    public void convertAndSend(String noticeTopic, MemoryCacheNoticeMsg noticeMsg) {

        redisTemplate.convertAndSend(noticeTopic, noticeMsg);
    }


}
