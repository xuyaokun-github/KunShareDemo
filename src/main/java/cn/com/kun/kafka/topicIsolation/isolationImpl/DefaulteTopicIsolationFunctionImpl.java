package cn.com.kun.kafka.topicIsolation.isolationImpl;

import cn.com.kun.kafka.topicIsolation.bean.TopicBean;
import cn.com.kun.kafka.topicIsolation.enums.TopicPriorityEnum;
import cn.com.kun.kafka.topicIsolation.properties.KafkaTopicIsolationProperties;
import cn.com.kun.kafka.topicIsolation.properties.TopicBizType;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *
 *
 * author:xuyaokun_kzx
 * date:2022/12/30
 * desc:
*/
@Primary
@Component
public class DefaulteTopicIsolationFunctionImpl implements TopicIsolationFunction{

    @Override
    public void initTopic(KafkaTopicIsolationProperties kafkaTopicIsolationProperties, Map<String, Map<String, TopicBean>> topicBeansMap) {

        List<TopicBizType> topicBizTypeList = kafkaTopicIsolationProperties.getBizTypes();
        if (topicBizTypeList.get(0) != null && topicBizTypeList.get(1) != null){
            //
            List<String> bizValues = topicBizTypeList.get(0).getBizValues();
            List<String> bizValues2 = topicBizTypeList.get(1).getBizValues();
            for (String str : bizValues){
                //渠道
                Map<String, TopicBean> topicBeanMap = topicBeansMap.get(str);
                if (topicBeanMap == null){
                    topicBeanMap = new ConcurrentHashMap<>();
                    topicBeansMap.put(str, topicBeanMap);
                }
                for (String str2 : bizValues2){
                    TopicBean topicBean = null;
                    //排列组合方式添加，基本上都齐了
                    for(TopicPriorityEnum topicPriorityEnum : TopicPriorityEnum.values()) {
                        topicBean = new TopicBean(kafkaTopicIsolationProperties.getTopicPrefix(), Arrays.asList(str, str2), topicPriorityEnum.name());
                        topicBeanMap.put(str2 + "-" + topicPriorityEnum.name(), topicBean);
                    }
                }
            }

        }
    }

    @Override
    public String getTopic(String channel, String batchFlag, String templatePriority, Map<String, Map<String, TopicBean>> topicBeansMap) {

        Map<String, TopicBean> topicBeanMap = topicBeansMap.get(channel);
        if (topicBeanMap != null){
            TopicBean topicBean = topicBeanMap.get(batchFlag + "-" + templatePriority);
            if (topicBean != null){
                return topicBean.buildTopicName();
            }
        }
        //假如返回空，上层会返回默认主题（配置文件定义的）
        return null;
    }

}
