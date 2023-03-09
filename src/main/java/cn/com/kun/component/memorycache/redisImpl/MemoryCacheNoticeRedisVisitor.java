package cn.com.kun.component.memorycache.redisImpl;

import cn.com.kun.component.memorycache.vo.MemoryCacheNoticeMsg;

import java.util.Map;

/**
 * Redis访问器
 * 假如项目里没有用RedisTemplate，可以参考DefaultMemoryCacheNoticeRedisVisitor实现自定义一个MemoryCacheNoticeRedisVisitor
 * author:xuyaokun_kzx
 * desc:
*/
public interface MemoryCacheNoticeRedisVisitor {

    Map<Object, Object> getHash(String keyName);

    void put(String keyName, String hashKeyName, String value);

    void convertAndSend(String noticeTopic, MemoryCacheNoticeMsg noticeMsg);
}
