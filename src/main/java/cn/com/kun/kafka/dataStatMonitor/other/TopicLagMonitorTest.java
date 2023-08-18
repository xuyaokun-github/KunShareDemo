package cn.com.kun.kafka.dataStatMonitor.other;

import cn.com.kun.kafka.dataStatMonitor.lag.TopicLagMonitor;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class TopicLagMonitorTest {


    public static void main(String[] args) {

        // Kafka客户端配置
        Properties props = new Properties();
        props.put("bootstrap.servers", "ip:port");
        props.put("group.id", "attack-consumer");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());

        // 创建KafkaConsumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        TopicLagMonitor topicLagMonitor = new TopicLagMonitor(null);

        long backlog = topicLagMonitor.getTotalLagInfo("topic-test");
        System.out.println(backlog);

        topicLagMonitor.getAllTopicsLagInfo().forEach((topic, backlogCount) -> System.out.println(topic + " - " + backlogCount));
    }


}
