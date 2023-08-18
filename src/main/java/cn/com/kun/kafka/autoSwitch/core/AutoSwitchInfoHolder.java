package cn.com.kun.kafka.autoSwitch.core;

import cn.com.kun.kafka.autoSwitch.enums.KafkaClusterStatusEnum;
import cn.com.kun.kafka.autoSwitch.vo.KafkaCluster;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 对于生产者
 * 不同的主题可以同时发往不同的集群(为什么会有这种场景，这个是不是伪需求吗？)
 *
 * author:xuyaokun_kzx
 * date:2023/5/5
 * desc:
*/
public class AutoSwitchInfoHolder {

    private final static Logger LOGGER = LoggerFactory.getLogger(AutoSwitchInfoHolder.class);

    /**
     * 目标集群（来自配置文件）
     */
    public static String targetCluster = "";

    /**
     * 可用的集群集合
     *
     */
    private static Map<String, String> topicClusterMap = new ConcurrentHashMap<>();

    private static Map<String, KafkaCluster> kafkaClusterMap = new ConcurrentHashMap<>();

    /**
     * 获取最佳目标集群
     *
     * 判断优先级：
     * 目标集群（来自配置文件） > Topic所属集群
     *
     * 假如定义了目标集群，且该集群可用，采用目标集群
     * 假如目标集群没配，则以Topic所属集群为准
     *
     * @param topic
     * @return
     */
    public static KafkaCluster getBestTargetCluster(String topic) {

        KafkaCluster kafkaCluster = kafkaClusterMap.get(targetCluster);
        if (kafkaCluster != null && KafkaClusterStatusEnum.ENABLED.equals(kafkaCluster.getStatus())){
            //目标集群可用，直接返回可用集群
            return kafkaCluster;
        }

        //说明目标集群不可用，则以Topic为准找匹配集群
        String kafkaClusterName = topicClusterMap.get(topic);
        if (StringUtils.isNotEmpty(kafkaClusterName)){
            kafkaCluster = kafkaClusterMap.get(kafkaClusterName);
            if (kafkaCluster != null && KafkaClusterStatusEnum.ENABLED.equals(kafkaCluster.getStatus())){
                return kafkaCluster;
            }
        }

        //无可用集群
        if (kafkaCluster == null){
            LOGGER.error("无可用Kafka集群");
            throw new RuntimeException("无可用Kafka集群");
        }
        return kafkaCluster;
    }

    public static void refreshClusterInfo(List<KafkaCluster> kafkaClusterList) {

        kafkaClusterMap = kafkaClusterList.stream().collect(Collectors.toMap(KafkaCluster::getName, a -> a, (k1, k2) -> k1));
        kafkaClusterMap.size();

    }


    public static void refreshTopicClusterInfo(Map<String, String> map) {
        topicClusterMap = map;
    }

}
