package cn.com.kun.kafka.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Properties;

@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Configuration
public class KafkaConsumerConfg {

    @Autowired
    KafkaConsumerProperties kafkaConsumerProperties;

    @Bean
    public KafkaConsumer<String, String> helloTopicKafkaConsumer(){
        Properties props = buildConsumerProperties();
//        props.put("max.poll.interval.ms", kafkaConsumerProperties.getMaxPollIntervalMs());//拉取间隔(千万不要设置太小)
        //KafkaConsumer类不是线程安全的
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("KUNGHSU_MSG_CACHE_SM01_BATCH"));
        return consumer;
    }

    /**
     * 同时订阅多个topic
     * @return
     */
    @Bean
    public KafkaConsumer<String, String> helloTopicKafkaConsumerMore(){
        Properties props = buildConsumerProperties();
//        props.put("max.poll.interval.ms", kafkaConsumerProperties.getMaxPollIntervalMs());//拉取间隔(千万不要设置太小)
        //KafkaConsumer类不是线程安全的
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        //在定义的时候，可以不用订阅主题
        consumer.subscribe(Arrays.asList("hello-topic"));
//        consumer.subscribe(Arrays.asList("hello-topic", "hello-topic2"));
//        consumer.subscribe(Arrays.asList("hello-topic", "hello-topic2", "hello-topic3"));
        return consumer;
    }

    private Properties buildConsumerProperties() {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaConsumerProperties.getBootstrapServers());
        props.put("group.id", kafkaConsumerProperties.getGroupId());//设置成同一个组ID,模拟多节点
        props.put("enable.auto.commit", kafkaConsumerProperties.getEnableAutoCommit());//手动提交
        props.put("auto.commit.interval.ms", kafkaConsumerProperties.getAutoCommitIntervalMs());
        props.put("auto.offset.reset", kafkaConsumerProperties.getAutoOffsetReset());
        props.put("max.poll.records", kafkaConsumerProperties.getMaxPollRecords());//每次拉取条数
        props.put("max.partition.fetch.bytes", kafkaConsumerProperties.getMaxPartitionFetchBytes());//每次拉取条数
        props.put("max.poll.interval.ms", kafkaConsumerProperties.getMaxPollIntervalMs());//拉取间隔(千万不要设置太小)
        props.put("key.deserializer", kafkaConsumerProperties.getKeyDeserializer());
        props.put("value.deserializer", kafkaConsumerProperties.getValueDeserializer());
        return props;
    }

}
