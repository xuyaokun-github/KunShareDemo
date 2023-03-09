package cn.com.kun.component.memorycache.redisImpl;

import cn.com.kun.component.memorycache.vo.MemoryCacheNoticeMsg;

import java.util.Map;

public interface MemoryCacheNoticeRedisVisitor {

    Map<Object, Object> getHash(String keyName);

    void put(String keyName, String hashKeyName, String value);

    void convertAndSend(String noticeTopic, MemoryCacheNoticeMsg noticeMsg);
}
