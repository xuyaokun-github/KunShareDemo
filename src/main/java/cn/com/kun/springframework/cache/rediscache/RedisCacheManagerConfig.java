package cn.com.kun.springframework.cache.rediscache;

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

        /**
         * 可以定义专门给某个服务层用的配置，例如下面这个配置，只给userservice使用
         */
        RedisCacheConfiguration userServiceRedisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer()))
                .prefixKeysWith("RedisCacheManager-user-")//设置key前缀
                .entryTtl(Duration.ofDays(1));//设置Redis缓存有效期为1天
        Map<String, RedisCacheConfiguration> initialCacheConfigurations = new HashMap<>();
        initialCacheConfigurations.put("user-service", userServiceRedisCacheConfiguration);
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
