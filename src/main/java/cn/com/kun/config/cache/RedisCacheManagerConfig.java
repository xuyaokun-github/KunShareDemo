package cn.com.kun.config.cache;

import cn.com.kun.common.enums.RedisCacheConfigurantionsEnum;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * RedisCacheManager配置
 * spring-cache的redis实现
 *
 * author:xuyaokun_kzx
 * date:2021/5/19
 * desc:
*/
@Configuration
public class RedisCacheManagerConfig {

    /**
     * 为了区分开各个微服务写的内容
     * 因为有些项目组是多个微服务共用一个redis
     */
    private final String COMMON_PREFIX = "KungShare-RedisCacheManager:";


    @Primary
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

        /**
         * 只能通过RedisCacheConfiguration类的静态方法创建RedisCacheConfiguration对象
         * 有多个静态方法可用
         */
        //这里创建的是默认配置
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer())) //引入序列化方式
                .prefixKeysWith("RedisCacheManager-Default-")//设置key前缀
                .entryTtl(Duration.ofDays(1));//设置Redis缓存有效期为1天

        Map<String, RedisCacheConfiguration> initialCacheConfigurations = new HashMap<>();
        addCacheConfigurations(initialCacheConfigurations);
        /**
         * 注意，创建RedisCacheManager有多个重载的方法
         * org.springframework.data.redis.cache.RedisCacheManager#RedisCacheManager(
         * org.springframework.data.redis.cache.RedisCacheWriter,
         * org.springframework.data.redis.cache.RedisCacheConfiguration, java.util.Map)
         * 方法支持传入多个缓存管理器，类似CaffeineCache的用法
         * 然后在注解使用时，指定这个缓存管理器的名字
         */
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration, initialCacheConfigurations);
    }

    private void addCacheConfigurations(Map<String, RedisCacheConfiguration> initialCacheConfigurations) {

        //遍历枚举类，放入initialCacheConfigurations
        for(RedisCacheConfigurantionsEnum configurantionsEnum : RedisCacheConfigurantionsEnum.values()) {
            RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                    .prefixKeysWith(COMMON_PREFIX + configurantionsEnum.getPrefixKey())//设置key前缀
                    .entryTtl(configurantionsEnum.getEntryTtl());//设置Redis缓存有效期
            initialCacheConfigurations.put(configurantionsEnum.getConfigName(), redisCacheConfiguration);
        }
    }

    /**
     * 序列化方式默认是JDK的，
     * 可以用默认的，也可以自定义引入一个json序列化
     *
     * @return
     */
//    @Bean
    public RedisSerializer redisSerializer() {

        //创建JSON序列化器
        Jackson2JsonRedisSerializer serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(objectMapper);
        return serializer;

    }


}
