package cn.com.kun.kafka.dataStatMonitor.other;

import cn.com.kun.kafka.config.KafkaConsumerProperties;
import cn.com.kun.kafka.dataStatMonitor.lag.TopicLagMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataStatMonitorConfig {

    @Autowired
    KafkaConsumerProperties kafkaConsumerProperties;

    /**
     * 定义主题Lag监控器
     *
     * @return
     */
    @Bean
    public TopicLagMonitor topicLagMonitor(){

//        // Kafka客户端配置
//        Properties props = new Properties();
//        props.put("bootstrap.servers", kafkaConsumerProperties.getBootstrapServers());
//        //专门定义一个监控消费组（但是有缺点）
////        props.put("group.id", "monitor-consumer");
//        props.put("group.id", kafkaConsumerProperties.getGroupId());
//        props.put("key.deserializer", StringDeserializer.class.getName());
//        props.put("value.deserializer", StringDeserializer.class.getName());
//
//        props.put("enable.auto.commit", kafkaConsumerProperties.getEnableAutoCommit());//手动提交
//        props.put("auto.commit.interval.ms", kafkaConsumerProperties.getAutoCommitIntervalMs());
//        props.put("auto.offset.reset", kafkaConsumerProperties.getAutoOffsetReset());
//        props.put("max.poll.records", kafkaConsumerProperties.getMaxPollRecords());//每次拉取条数
//        props.put("max.partition.fetch.bytes", kafkaConsumerProperties.getMaxPartitionFetchBytes());//每次拉取条数
//        props.put("max.poll.interval.ms", kafkaConsumerProperties.getMaxPollIntervalMs());//拉取间隔(千万不要设置太小)
//        props.put("key.deserializer", kafkaConsumerProperties.getKeyDeserializer());
//        props.put("value.deserializer", kafkaConsumerProperties.getValueDeserializer());
//
//        // 创建KafkaConsumer
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

//        return new TopicLagMonitor(consumer);
        TopicLagMonitor topicLagMonitor = new TopicLagMonitor(kafkaConsumerProperties.getBootstrapServers(), kafkaConsumerProperties.getGroupId());
        topicLagMonitor.setLimitIntervalMs(1000);
        return topicLagMonitor;

    }


}
