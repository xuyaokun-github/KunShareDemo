package cn.com.kun.kafka.dataStatMonitor.runner;

import cn.com.kun.kafka.dataStatMonitor.lag.TopicLagMonitor;
import cn.com.kun.kafka.dataStatMonitor.properties.KafkaDataStatMonitorProperties;
import cn.com.kun.kafka.dataStatMonitor.scheduled.DataReportScheduleTask;
import cn.com.kun.kafka.dataStatMonitor.scheduled.LagCheckScheduleTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 *
 * author:xuyaokun_kzx
 * date:2023/8/21
 * desc:
*/
@Component
public class KafkaDataStatMonitorRunner implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaDataStatMonitorRunner.class);

    @Autowired
    private KafkaDataStatMonitorProperties kafkaDataStatMonitorProperties;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {


        if(kafkaDataStatMonitorProperties.isLagCheckEnabled()){

            if (kafkaDataStatMonitorProperties.getNormalTimeIntervalMs() > 0){
                LagCheckScheduleTask.initNormalTimeIntervalMs(kafkaDataStatMonitorProperties.getNormalTimeIntervalMs());
            }
            //判断是否创建TopicLagMonitor 创建TopicLagMonitor的逻辑交给定时任务工程
            TopicLagMonitor topicLagMonitor = applicationContext.getBean(TopicLagMonitor.class);
            if (topicLagMonitor != null){
                LagCheckScheduleTask.initSchedule(topicLagMonitor);
            }else {
                LOGGER.warn("kafka数据统计监控器组件的lagCheckEnabled属性为true，但未定义TopicLagMonitor Bean,请检查！");
            }

        }

        if(kafkaDataStatMonitorProperties.isDataReportEnabled()){

            DataReportScheduleTask.initSchedule();
        }


    }



}
