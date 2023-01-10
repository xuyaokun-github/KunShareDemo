package cn.com.kun.kafka.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Configuration
public class KafkaProducerConfig {

    @Autowired
    KafkaProducerProperties kafkaProducerProperties;

    @Bean
    public Producer<String, String> msgCacheTopicKafkaProducer(){

        Properties props = buildProducerProperties();
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
