package cn.com.kun.kafka.autoSwitch.other;

import cn.com.kun.kafka.autoSwitch.decorator.KafkaProducerDecorator;
import cn.com.kun.kafka.autoSwitch.factory.KafkaProducerFactory;
import cn.com.kun.kafka.config.KafkaProducerProperties;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Configuration
public class KafkaAutoSwitchProducerDemoConfig {

    @Autowired
    private KafkaProducerProperties kafkaProducerProperties;

    /**
     *
     * @return
     */
    @Bean
    public Producer<String, String> autoSwitchKafkaProducer(){

//        KafkaProducerDecorator decorator = new KafkaProducerDecorator(buildProducerImpl("hello-topic"));
        //懒加载
        KafkaProducerDecorator decorator = new KafkaProducerDecorator(null, "autoswitch-topic");
        decorator.setKafkaProducerFactory(new KafkaProducerFactory() {
            @Override
            public Producer buildProducer(String topic, String address) {

                return buildProducerImpl(topic, address);
            }
        });
        return decorator;
    }


    @Bean
    public Producer<String, String> autoSwitchKafkaProducer2(){

//        KafkaProducerDecorator decorator = new KafkaProducerDecorator(buildProducerImpl("hello-topic"));
        //懒加载
        KafkaProducerDecorator decorator = new KafkaProducerDecorator(null, "autoswitch-topic2");
        decorator.setKafkaProducerFactory(new KafkaProducerFactory() {
            @Override
            public Producer buildProducer(String topic, String address) {

                return buildProducerImpl(topic, address);
            }
        });
        return decorator;
    }

    /**
     * 自定义实现
     * @param topic
     * @param address
     * @return
     */
    private Producer buildProducerImpl(String topic, String address) {

        Properties props = buildProducerProperties();

        if (StringUtils.isNotEmpty(address)){
            props.put("bootstrap.servers", address);
        }

        //构建拦截链
        List<String> interceptors = new ArrayList<>();
        interceptors.add("cn.com.kun.kafka.interceptor.CounterInterceptor");
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, interceptors);

        Producer<String, String> producer = new KafkaProducer<>(props);
        return producer;
    }


    private Properties buildProducerProperties() {

        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaProducerProperties.getBootstrapServers());
        props.put("key.serializer", kafkaProducerProperties.getKeySerializer());
        // 使用Confluent实现的KafkaAvroSerializer
        props.put("value.serializer", kafkaProducerProperties.getValueSerializer());
        //ack
        props.put("acks", "all");
        //发生错误时，重传次数
        props.put("retries", 0);
        //Producer可以将发往同一个Partition的数据做成一个Produce Request发送请求，即Batch批处理，以减少请求次数，该值即为每次批处理的大小
        props.put("batch.size", 16384);
        //例如，设置linger.ms=5，会减少request发送的数量，但是在无负载下会增加5ms的发送时延。
        props.put("linger.ms", 1);
        //Producer可以用来缓存数据的内存大小
        props.put("buffer.memory", 33554432);
        return props;
    }


}
