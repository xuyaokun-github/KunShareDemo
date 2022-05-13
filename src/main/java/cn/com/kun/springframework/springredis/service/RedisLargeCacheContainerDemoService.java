package cn.com.kun.springframework.springredis.service;

import cn.com.kun.springframework.springredis.largeCacheContainer.RedisLargeCacheContainer;
import cn.com.kun.springframework.springredis.vo.UserRedisVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisLargeCacheContainerDemoService {

    @Autowired
    private RedisLargeCacheContainer<UserRedisVO> redisLargeCacheContainer;


    public void save(UserRedisVO userRedisVO) {

        boolean flag = redisLargeCacheContainer.save(userRedisVO);
    }

    public long size() {

        return redisLargeCacheContainer.size();
    }

    public void update(UserRedisVO userRedisVO) {
        boolean flag = redisLargeCacheContainer.update(userRedisVO);
    }

    public UserRedisVO get(String queryKey) {

        return redisLargeCacheContainer.get(queryKey, UserRedisVO.class);
    }

    public void delete() {
        boolean flag = redisLargeCacheContainer.clear();
    }
}
