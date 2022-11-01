package cn.com.kun.config.jedis;

import cn.com.kun.common.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@ConditionalOnProperty(prefix = "kunsharedemo.jedis", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Configuration
public class JedisConfiguration {

    @Autowired
    JedisConfigProperties redisConfig;

    @Bean
    public JedisPool jedisPoolConfig() {

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(redisConfig.getMaxTotal());
        jedisPoolConfig.setMaxIdle(redisConfig.getMaxIdle());
        jedisPoolConfig.setMinIdle(redisConfig.getMinIdle());
//        jedisPoolConfig.setMaxWaitMillis(redisConfig.getMaxWaitMillis());
//        jedisPoolConfig.setTestOnBorrow(redisConfig.isTestOnBorrow());
//        jedisPoolConfig.setTestOnReturn(redisConfig.isTestOnReturn());
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(1000);
        jedisPoolConfig.setTestWhileIdle(true);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(3000);

        JedisPool pool = new JedisPool(jedisPoolConfig, redisConfig.getHost(), redisConfig.getPort(), 100000);

        //初始化工具类
        JedisUtils.setPool(pool);

        return pool;
    }

    //集群模式 TODO
    public JedisCluster jedisCluster() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();

        return null;
    }

}
