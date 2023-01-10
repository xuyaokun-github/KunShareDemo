package cn.com.kun.kafka.topicIsolation.other;

import cn.com.kun.kafka.config.KafkaConsumerProperties;
import cn.com.kun.kafka.topicIsolation.consumer.KafkaConsumerProvider;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 实现类（不属于组件内的内容）
 *
 * author:xuyaokun_kzx
 * date:2023/1/6
 * desc:
*/
@Component
public class CustomKafkaConsumerProviderImpl implements KafkaConsumerProvider {

    @Autowired
    private KafkaConsumerProperties kafkaConsumerProperties;

    @Override
    public KafkaConsumer buildKafkaConsumer() {

        Properties props = buildConsumerProperties();
        //KafkaConsumer类不是线程安全的
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
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
