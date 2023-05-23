package cn.com.kun.kafka.autoSwitch.scheduled;

import cn.com.kun.kafka.autoSwitch.core.AutoSwitchInfoHolder;
import cn.com.kun.kafka.autoSwitch.core.TopicControlCenterVisitor;
import cn.com.kun.kafka.autoSwitch.properties.KafkaAutoSwitchProperties;
import cn.com.kun.kafka.autoSwitch.vo.ControlCenterQryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KafkaAutoSwitchConsumerScheduleTask {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaAutoSwitchConsumerScheduleTask.class);

    private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private static TopicControlCenterVisitor centerVisitor = null;

    public static void initSchedule(KafkaAutoSwitchProperties kafkaAutoSwitchProperties, TopicControlCenterVisitor visitor) {

        centerVisitor = visitor;
        scheduledExecutorService.scheduleAtFixedRate(()->{

            LOGGER.info("KafkaAutoSwitchConsumerScheduleTask Running");
            try {
                //访问控制中心，获取所有可用集群
                ControlCenterQryResult controlCenterQryResult = centerVisitor.qryClusters();
                if (controlCenterQryResult.isSuccess()){
                    AutoSwitchInfoHolder.refreshClusterInfo(controlCenterQryResult.getKafkaClusterList());
                    AutoSwitchInfoHolder.refreshTopicClusterInfo(controlCenterQryResult.getTopicClusterMap());
                }else {
                    //
                    LOGGER.error("TopicControlCenter查询失败");
                }

                //输出遍历
//                clusterQryResult.getKafkaClusterList().forEach(kafkaCluster -> {
//                    System.out.println(kafkaCluster.toString());
//                });

            }catch (Exception e){
                LOGGER.error("KafkaAutoSwitchProducerScheduleTask异常", e);
            }
        }, 0L, 10L, TimeUnit.SECONDS);
    }


}
