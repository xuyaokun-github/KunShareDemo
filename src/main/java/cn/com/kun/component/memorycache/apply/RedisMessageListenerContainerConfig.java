package cn.com.kun.component.memorycache.apply;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import static cn.com.kun.component.memorycache.constants.MemoryCacheConstants.NOTICE_TOPIC;

/**
 * Redis实现相关Bean定义
 *
 * author:xuyaokun_kzx
 * date:2023/1/19
 * desc:
*/
@ConditionalOnExpression("'${memorycache.role}'.equals('All') || '${memorycache.role}'.equals('Apply')")
//@ConditionalOnProperty(prefix = "memorycache", value = {"role"}, havingValue = "All", matchIfMissing = true)
//@ConditionalOnProperty(prefix = "memorycache", value = {"role"}, havingValue = "Maintain", matchIfMissing = true)
@Configuration
public class RedisMessageListenerContainerConfig {


    //创建接收通知监听器
    @Bean
    public MemoryCacheNoticeRedisListener memoryCacheNoticeListener() {
        return new MemoryCacheNoticeRedisListener();
    }

    //定义一个主题
    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(NOTICE_TOPIC);
    }

    /**
     * 创建一个监听器容器,依赖应用本身的Redis连接池
     */
    @Bean
    public RedisMessageListenerContainer memoryCacheRedisMessageListenerContainer(RedisConnectionFactory connectionFactory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(memoryCacheNoticeListener(), topic());
        return container;
    }

}
