package cn.com.kun.kafka.topicIsolation.runner;

import cn.com.kun.kafka.topicIsolation.consumer.ConsumerTopicProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsumerTopicProcessorRunner implements CommandLineRunner {

    @Autowired
    private ConsumerTopicProcessor consumerTopicProcessor;

    @Override
    public void run(String... args) throws Exception {
        consumerTopicProcessor.start();
    }

}
