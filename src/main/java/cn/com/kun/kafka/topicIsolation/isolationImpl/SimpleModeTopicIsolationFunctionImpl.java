package cn.com.kun.kafka.topicIsolation.isolationImpl;

import cn.com.kun.kafka.topicIsolation.bean.TopicBean;
import cn.com.kun.kafka.topicIsolation.properties.KafkaTopicIsolationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 假如自定义了扩展实现，则配置文件必须指定使用该实现，填上bean名
 * author:xuyaokun_kzx
 * date:2022/12/30
 * desc:
*/
@Component
public class SimpleModeTopicIsolationFunctionImpl implements TopicIsolationFunction {

    @Override
    public void initTopic(KafkaTopicIsolationProperties kafkaTopicIsolationProperties, Map<String, Map<String, TopicBean>> topicBeansMap) {

        //注册

    }

    @Override
    public String getTopic(String channel, String batchFlag, String templatePriority, Map<String, Map<String, TopicBean>> topicBeansMap) {

        return null;
    }

}
