package cn.com.kun.kafka.topicIsolation.isolationImpl;

import cn.com.kun.kafka.topicIsolation.bean.TopicBean;
import cn.com.kun.kafka.topicIsolation.properties.KafkaTopicIsolationProperties;

import java.util.Map;

/**
 * 定义多少套实现，自由决定
 *
 * author:xuyaokun_kzx
 * date:2022/12/30
 * desc:
*/
public interface TopicIsolationFunction {

    void initTopic(KafkaTopicIsolationProperties kafkaTopicIsolationProperties, Map<String, Map<String, TopicBean>> topicBeansMap);

    String getTopic(String channel, String batchFlag, String templatePriority, Map<String, Map<String, TopicBean>> topicBeansMap);

}
