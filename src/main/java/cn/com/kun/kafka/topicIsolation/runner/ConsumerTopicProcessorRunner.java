package cn.com.kun.kafka.topicIsolation.runner;

import cn.com.kun.kafka.topicIsolation.consumer.ConsumerTopicProcessor;
import cn.com.kun.kafka.topicIsolation.properties.KafkaTopicIsolationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsumerTopicProcessorRunner implements CommandLineRunner {

    @Autowired
    private KafkaTopicIsolationProperties kafkaTopicIsolationProperties;

    @Autowired
    private ConsumerTopicProcessor consumerTopicProcessor;

    @Override
    public void run(String... args) throws Exception {

        //Kafka主题拆分组件-消费者 未启用
        if(!kafkaTopicIsolationProperties.isConsumerEnabled()){
            return;
        }

        consumerTopicProcessor.start();
    }

}
