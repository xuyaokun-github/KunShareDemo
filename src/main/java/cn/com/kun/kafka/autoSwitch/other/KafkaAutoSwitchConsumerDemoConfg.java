package cn.com.kun.kafka.autoSwitch.other;

import cn.com.kun.kafka.autoSwitch.decorator.KafkaConsumerDecorator;
import cn.com.kun.kafka.config.KafkaConsumerProperties;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Properties;

@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Configuration
public class KafkaAutoSwitchConsumerDemoConfg {

    @Autowired
    KafkaConsumerProperties kafkaConsumerProperties;

    @Bean
    public Consumer<String, String> autoswitchTopicKafkaConsumer(){
        Properties props = buildConsumerProperties();
//        props.put("max.poll.interval.ms", kafkaConsumerProperties.getMaxPollIntervalMs());//拉取间隔(千万不要设置太小)
        //KafkaConsumer类不是线程安全的
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("autoswitch-topic"));

        KafkaConsumerDecorator consumerDecorator = new KafkaConsumerDecorator(consumer);
        return consumerDecorator;
    }

    @Bean
    public Consumer<String, String> autoswitchTopicKafkaConsumer2(){
        Properties props = buildConsumerProperties();
//        props.put("max.poll.interval.ms", kafkaConsumerProperties.getMaxPollIntervalMs());//拉取间隔(千万不要设置太小)
        //KafkaConsumer类不是线程安全的
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("autoswitch-topic2"));
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
