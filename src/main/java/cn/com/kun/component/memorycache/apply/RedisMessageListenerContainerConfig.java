package cn.com.kun.component.memorycache.apply;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import static cn.com.kun.component.memorycache.MemoryCacheConstants.NOTICE_TOPIC;

@Configuration
public class RedisMessageListenerContainerConfig {


    //创建一个监听器
    @Bean
    public MemoryCacheNoticeListener consumerRedis() {
        return new MemoryCacheNoticeListener();
    }

    //定义一个主题
    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(NOTICE_TOPIC);
    }

    //创建一个监听器容器
    @Bean
    public RedisMessageListenerContainer customRedisMessageListenerContainer(RedisConnectionFactory connectionFactory) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(consumerRedis(), topic());
        return container;
    }

}
