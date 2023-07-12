package cn.com.kun.kafka.dynamicConsume.other;

import cn.com.kun.kafka.config.KafkaConsumerProperties;
import cn.com.kun.kafka.dynamicConsume.DynamicKafkaConsumer;
import cn.com.kun.kafka.dynamicConsume.extend.ConsumeSwitchQuerier;
import cn.com.kun.kafka.dynamicConsume.extend.KafkaConsumerBuilder;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Properties;

@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Configuration
public class PauseConsumeKafkaConsumerConfig {

    @Autowired
    private KafkaConsumerProperties kafkaConsumerProperties;

    @Bean
    public KafkaConsumer<String, String> customTopicOneKafkaConsumer(){
        Properties props = buildConsumerProperties();
//        props.put("max.poll.interval.ms", kafkaConsumerProperties.getMaxPollIntervalMs());//拉取间隔(千万不要设置太小)

        props.put("group.id", "pauseConsumeConsumerGroup");

        //KafkaConsumer类不是线程安全的
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("custom-topic1"));
        return consumer;
    }

    @Bean
    public Consumer<String, String> dynamicKafkaConsumer(KafkaConsumerBuilder consumerBuilder, ConsumeSwitchQuerier switchQuerier){

//        Properties props = buildConsumerProperties();
//        props.put("group.id", "pauseConsumeConsumerGroup");
//
//        //KafkaConsumer类不是线程安全的
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
//        consumer.subscribe(Arrays.asList("dynamic-consume-topic1"));

        Assert.notNull(consumerBuilder, "消费者构造器不能为空");
        Assert.notNull(switchQuerier, "消费开关查询器不能为空");

        //一开始可以设置为null,让它懒加载
        DynamicKafkaConsumer dynamicKafkaConsumer = new DynamicKafkaConsumer(null, consumerBuilder, switchQuerier);
        //设置目标订阅主题列表
        dynamicKafkaConsumer.setSubscribeTopicList(Arrays.asList("dynamic-consume-topic1"));

        return dynamicKafkaConsumer;
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
