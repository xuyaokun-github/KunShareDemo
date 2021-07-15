package cn.com.kun.config.kafka;

import cn.com.kun.common.vo.ResultVo;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE;

/**
 * ACK机制
 *
 * Created by xuyaokun On 2019/5/21 21:19
 * @desc:
 */
@Configuration
public class KafkaAckConfig {

    public final static Logger LOGGER = LoggerFactory.getLogger(KafkaAckConfig.class);

    public Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//自动提交设置为false
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    @Bean("kafkaAckContainerFactory")
    public ConcurrentKafkaListenerContainerFactory kafkaAckContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(ResultVo.class)));
        //要用ACK机制，在创建连接池时，就要开启 手动确认模式
        //默认是自动确认模式,设置成手动提交
        factory.getContainerProperties().setAckMode(MANUAL_IMMEDIATE);
        //MANUAL_IMMEDIATE在旧版jar包中是：AbstractMessageListenerContainer.AckMode.MANUAL_IMMEDIATE
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory(consumerProps()));
        return factory;
    }

}
