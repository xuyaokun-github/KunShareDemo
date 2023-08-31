package cn.com.kun.kafka.dataStatMonitor.runner;

import cn.com.kun.kafka.dataStatMonitor.properties.KafkaDataStatMonitorProperties;
import cn.com.kun.kafka.dataStatMonitor.scheduled.DataReportScheduleTask;
import cn.com.kun.kafka.dataStatMonitor.scheduled.LagCheckScheduleTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 *
 * author:xuyaokun_kzx
 * date:2023/8/21
 * desc:
*/
@Component
public class KafkaAutoSwitchRunner implements CommandLineRunner {

    @Autowired
    private KafkaDataStatMonitorProperties kafkaDataStatMonitorProperties;

    @Override
    public void run(String... args) throws Exception {


        //Kafka主题拆分组件-消费者 未启用
        if(kafkaDataStatMonitorProperties.isLagCheckEnabled()){

            LagCheckScheduleTask.initSchedule();
        }

        if(kafkaDataStatMonitorProperties.isDataReportEnabled()){

            DataReportScheduleTask.initSchedule();
        }


    }



}
