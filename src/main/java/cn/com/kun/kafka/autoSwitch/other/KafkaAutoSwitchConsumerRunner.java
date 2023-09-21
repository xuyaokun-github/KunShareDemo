package cn.com.kun.kafka.autoSwitch.other;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Component
public class KafkaAutoSwitchConsumerRunner implements CommandLineRunner, ApplicationContextAware {

    ApplicationContext context;

    private final static Logger logger = LoggerFactory.getLogger(KafkaAutoSwitchConsumerRunner.class);

    @Autowired
    private KafkaAutoSwitchConsumerDemoService kafkaAutoSwitchConsumerDemoService;

    @Autowired
    private KafkaAutoSwitchConsumerDemoService2 kafkaAutoSwitchConsumerDemoService2;

    public void run(String... args) {

//        kafkaAutoSwitchConsumerDemoService.init();
//        kafkaAutoSwitchConsumerDemoService2.init();
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
