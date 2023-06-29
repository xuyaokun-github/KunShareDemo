package cn.com.kun.kafka.lagMonitor;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;


import java.util.*;

/**
 * Created by xuyaokun On 2023/6/28 20:27
 * @desc:
 */
public class TopicBacklog {


    public static void main(String[] args) {

        int backlog = getTotalLagInfo("topic-test");
        System.out.println(backlog);

        getAllTopicsLagInfo().forEach((topic, backlogCount) -> System.out.println(topic + " - " + backlogCount));
    }

    public static int getTotalLagInfo(String topic) {

        // Kafka客户端配置
        Properties props = new Properties();
        props.put("bootstrap.servers", "ip:port");
        props.put("group.id", "attack-consumer");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        // 创建KafkaConsumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        // 订阅要查询的主题
        List<PartitionInfo> partitions = consumer.partitionsFor(topic);
        List<TopicPartition> topicPartitions = new ArrayList<>();
        for (PartitionInfo partition : partitions) {
            topicPartitions.add(new TopicPartition(partition.topic(), partition.partition()));
        }

        // 手动分配分区
        consumer.assign(topicPartitions);

        // 记录未消费消息总数
        int totalBacklog = 0;

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


    public static Map<String, Integer> getAllTopicsLagInfo() {

        // Kafka客户端配置
        Properties props = new Properties();
        props.put("bootstrap.servers", "ip:port");
        props.put("group.id", "attack-consumer");//消费者组
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

        //创建消费者组，但是不订阅主题
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        //获取所有主题列表
        Map<String, List<PartitionInfo>> topicMap = consumer.listTopics();

        //记录每个主题未消费消息总数 key:主题名 value: Lag值
        Map<String, Integer> lagInfoMap = new HashMap<>();

        //遍历每个主题,计算其未消费消息数
        for (String topic : topicMap.keySet()) {

            //订阅要查询的主题
            List<PartitionInfo> partitions = consumer.partitionsFor(topic);
            List<TopicPartition> topicPartitions = new ArrayList<>();
            for (PartitionInfo partition : partitions) {
                topicPartitions.add(new TopicPartition(partition.topic(), partition.partition()));
            }

            //手动分配分区（subscribe是订阅，assign只是分配）这个行为是否会影响正常的消费呢？
            consumer.assign(topicPartitions);

            int backlog = 0;
            //遍历所有分区
            for (PartitionInfo partition : partitions) {
                TopicPartition tp = new TopicPartition(partition.topic(), partition.partition());
                long latestOffset = consumer.position(tp);
                long endOffset = consumer.endOffsets(Collections.singleton(tp)).get(tp);
                backlog += Math.toIntExact(endOffset - latestOffset);
            }
            lagInfoMap.put(topic, backlog);
        }

        //返回每个主题未消费消息总数
        return lagInfoMap;
    }


}


