package cn.com.kun.springframework.springredis.largeCacheContainer;

import cn.com.kun.springframework.springredis.vo.UserRedisVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RedisLargeCacheContainerConfig {

    @Autowired
    private RedisTemplate redisTemplate;

    @Bean
    public RedisLargeCacheContainer<UserRedisVO> redisLargeCacheContainer(){

        return new RedisLargeCacheContainer(redisTemplate, "kunghsu-redis-lcc", new BuildKeyFunction<UserRedisVO>() {
            @Override
            public <T> String buildKey(T souceObj) {
                UserRedisVO userRedisVO = (UserRedisVO) souceObj;
                if (StringUtils.isEmpty(userRedisVO.getCustomerType()) || StringUtils.isEmpty(userRedisVO.getCustomerId())){
                    return null;
                }
                return userRedisVO.getCustomerType() + ":" +userRedisVO.getCustomerId();
            }
        }, 60L, TimeUnit.DAYS);
    }

}
