package cn.com.kun.kafka.autoSwitch.runner;

import cn.com.kun.kafka.autoSwitch.core.AutoSwitchInfoHolder;
import cn.com.kun.kafka.autoSwitch.core.TopicControlCenterVisitor;
import cn.com.kun.kafka.autoSwitch.properties.KafkaAutoSwitchProperties;
import cn.com.kun.kafka.autoSwitch.scheduled.KafkaAutoSwitchConsumerScheduleTask;
import cn.com.kun.kafka.autoSwitch.scheduled.KafkaAutoSwitchProducerScheduleTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 组件启动器
 *
 * author:xuyaokun_kzx
 * date:2023/5/4
 * desc:
*/
@Component
public class KafkaAutoSwitchRunner implements CommandLineRunner {

    @Autowired
    private KafkaAutoSwitchProperties kafkaAutoSwitchProperties;

    private TopicControlCenterVisitor visitor;

    @Override
    public void run(String... args) throws Exception {

        visitor = new TopicControlCenterVisitor(kafkaAutoSwitchProperties.getControlCenterUrl());
        AutoSwitchInfoHolder.targetCluster = kafkaAutoSwitchProperties.getTargetCluster();

        //Kafka主题拆分组件-消费者 未启用
        if(kafkaAutoSwitchProperties.isConsumerEnabled()){

            KafkaAutoSwitchConsumerScheduleTask.initSchedule(kafkaAutoSwitchProperties, visitor);
        }

        if(kafkaAutoSwitchProperties.isProducerEnabled()){

            KafkaAutoSwitchProducerScheduleTask.initSchedule(kafkaAutoSwitchProperties, visitor);
        }
    }



}
