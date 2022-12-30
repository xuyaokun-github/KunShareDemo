package cn.com.kun.controller.topicIsolation;

import cn.com.kun.kafka.topicIsolation.ProducerTopicProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/kafka-topic-isolation")
@RestController
public class KafkaTopicIsolationDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaTopicIsolationDemoController.class);

    @Autowired
    ProducerTopicProcessor producerTopicProcessor;

    @GetMapping("/test")
    public String test(){

        LOGGER.info(producerTopicProcessor.getTopic("SM01", "BATCH", "HIGH"));
        LOGGER.info(producerTopicProcessor.getTopic("SM01", "REAL", "HIGH"));
        LOGGER.info(producerTopicProcessor.getTopic("SM01", "REAL", "MIDDLE"));
        LOGGER.info(producerTopicProcessor.getTopic("SM01", "REAL", "LOW"));
        LOGGER.info(producerTopicProcessor.getTopic("SM01", "REAL", "LOW22222"));
        LOGGER.info(producerTopicProcessor.getTopic("SM11", "REAL", "MIDDLE"));


        return "test";
    }

}
