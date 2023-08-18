package cn.com.kun.kafka.dataStatMonitor.lag;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 主题堆积监控器
 *
 * Created by xuyaokun On 2023/6/28 20:27
 * @desc:
 */
public class TopicLagMonitor {

    private final static Logger LOGGER = LoggerFactory.getLogger(TopicLagMonitor.class);

    /**
     * 定义成单例，会有lag只增不减的情况（不应该用这种方式）
     */
    private KafkaConsumer<String, String> consumer;

    private String bootstrapServers;

    private String groupId;

    /**
     * 上一次监控的时间
     * 因为每次监控都会创建消费者，为了避免这种操作，做一个简单的优化防并发：
     * 假如上一次监控时间离当前不足1分钟(10秒钟)，返回空（或者可以放回上一次监控的值）
     *
     */
    private long lastMonitorTime;

    /**
     * 默认是10秒，防止并发太猛
     */
    private long limitIntervalMs = 10 * 1000;


    private Map<String, Long> lastMonitorLagMap = new HashMap<>();

    public TopicLagMonitor(KafkaConsumer consumer) {
        this.consumer = consumer;
    }

    public TopicLagMonitor(String bootstrapServers, String groupId) {
        this.bootstrapServers = bootstrapServers;
        this.groupId = groupId;
    }

    /**
     * 可自由定制监控灵敏度
     *
     * @param limitIntervalMs
     */
    public void setLimitIntervalMs(long limitIntervalMs) {
        this.limitIntervalMs = limitIntervalMs;
    }

    /**
     * 反例代码
     * consumer不能定义成单例
     *
     * @param topic
     * @return
     */
    public long getTotalLagInfo2(String topic) {

        // 订阅要查询的主题
        List<PartitionInfo> partitions = consumer.partitionsFor(topic);
        List<TopicPartition> topicPartitions = new ArrayList<>();
        for (PartitionInfo partition : partitions) {
            topicPartitions.add(new TopicPartition(partition.topic(), partition.partition()));
        }

        // 手动分配分区
        consumer.assign(topicPartitions);

        // 记录未消费消息总数
        long totalBacklog = 0;

        // 遍历每个分区获取其未消费消息数并累加
        for (PartitionInfo partition : partitions) {
            TopicPartition tp = new TopicPartition(partition.topic(), partition.partition());
            // 获取消费者的当前偏移量
            long latestOffset = consumer.position(tp);
            long endOffset = consumer.endOffsets(Collections.singleton(tp)).get(tp);
            int backlog = Math.toIntExact(endOffset - latestOffset);
            totalBacklog += backlog;
        }

        // 返回未消费消息总数
        return totalBacklog;
    }

    /**
     * 获取堆积量
     *
     * @param topic
     * @return
     */
    public long getTotalLagInfo(String topic) {

        if(!checkMonitorTime()){
            return lastMonitorLag(topic);
        }

        //每次都要创建消费者
        KafkaConsumer<String, String> consumer = buildKafkaConsumer();

        long totalBacklog = countLag(consumer, topic);

        //关闭消费者
        consumer.close();

        // 返回未消费消息总数
        lastMonitorTime = System.currentTimeMillis();
        lastMonitorLagMap.put(topic, totalBacklog);
        return totalBacklog;
    }

    private long lastMonitorLag(String topic) {

        Long lag = lastMonitorLagMap.get(topic);
        return lag != null ? lag : 0;
    }

    private boolean checkMonitorTime() {

        if (System.currentTimeMillis() - lastMonitorTime < limitIntervalMs){
            LOGGER.info("监控访问频率超限");
            return false;
        }

        return true;
    }

    private long countLag(KafkaConsumer<String, String> consumer, String topic) {

        // 订阅要查询的主题
        List<PartitionInfo> partitions = consumer.partitionsFor(topic);
        List<TopicPartition> topicPartitions = new ArrayList<>();
        for (PartitionInfo partition : partitions) {
            topicPartitions.add(new TopicPartition(partition.topic(), partition.partition()));
        }

        // 手动分配分区
        consumer.assign(topicPartitions);

        // 记录未消费消息总数
        long totalBacklog = 0;

        // 遍历每个分区获取其未消费消息数并累加
        for (PartitionInfo partition : partitions) {
            TopicPartition tp = new TopicPartition(partition.topic(), partition.partition());
            // 获取消费者的当前偏移量
            long latestOffset = consumer.position(tp);
            long endOffset = consumer.endOffsets(Collections.singleton(tp)).get(tp);
            int backlog = Math.toIntExact(endOffset - latestOffset);
            totalBacklog += backlog;
        }
        return totalBacklog;
    }

    private KafkaConsumer<String, String> buildKafkaConsumer() {

        // Kafka客户端配置
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        //专门定义一个监控消费组（但是有缺点）
//        props.put("group.id", "monitor-consumer");
        props.put("group.id", groupId);
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

//        props.put("enable.auto.commit", kafkaConsumerProperties.getEnableAutoCommit());//手动提交
//        props.put("auto.commit.interval.ms", kafkaConsumerProperties.getAutoCommitIntervalMs());
//        props.put("auto.offset.reset", kafkaConsumerProperties.getAutoOffsetReset());
//        props.put("max.poll.records", kafkaConsumerProperties.getMaxPollRecords());//每次拉取条数
//        props.put("max.partition.fetch.bytes", kafkaConsumerProperties.getMaxPartitionFetchBytes());//每次拉取条数
//        props.put("max.poll.interval.ms", kafkaConsumerProperties.getMaxPollIntervalMs());//拉取间隔(千万不要设置太小)
//        props.put("key.deserializer", kafkaConsumerProperties.getKeyDeserializer());
//        props.put("value.deserializer", kafkaConsumerProperties.getValueDeserializer());

        // 创建KafkaConsumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        return consumer;
    }


    public Map<String, Long> getAllTopicsLagInfo() {

        //每次都要创建消费者
        KafkaConsumer<String, String> consumer = buildKafkaConsumer();
        //获取所有主题列表
        Map<String, List<PartitionInfo>> topicMap = consumer.listTopics();

        //记录每个主题未消费消息总数 key:主题名 value: Lag值
        Map<String, Long> lagInfoMap = new HashMap<>();

        //遍历每个主题,计算其未消费消息数
        for (String topic : topicMap.keySet()) {

            long totalBacklog = countLag(consumer, topic);
            lagInfoMap.put(topic, totalBacklog);
        }

        //关闭消费者
        consumer.close();

        //返回每个主题未消费消息总数
        return lagInfoMap;
    }


}


