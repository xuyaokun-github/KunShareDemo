package cn.com.kun.kafka.topicIsolation.isolationImpl;

import cn.com.kun.kafka.topicIsolation.bean.TopicBean;
import cn.com.kun.kafka.topicIsolation.enums.TopicPriorityEnum;
import cn.com.kun.kafka.topicIsolation.properties.KafkaTopicIsolationProperties;
import cn.com.kun.kafka.topicIsolation.properties.TopicBizType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MoreChannelModeTopicIsolationFunctionImpl implements TopicIsolationFunction{

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
                    //这里的BATCH其实就是定义在配置文件里的业务字段值
                    if (!"BATCH".equals(str2)){
                        //假如不是批量
                        topicBean = new TopicBean(kafkaTopicIsolationProperties.getTopicPrefix(), Arrays.asList(str), TopicPriorityEnum.HIGH.name());
                        topicBeanMap.put(TopicPriorityEnum.HIGH.name(), topicBean);
                        topicBean = new TopicBean(kafkaTopicIsolationProperties.getTopicPrefix(), Arrays.asList(str), TopicPriorityEnum.MIDDLE.name());
                        topicBeanMap.put(TopicPriorityEnum.MIDDLE.name(), topicBean);
                        topicBean = new TopicBean(kafkaTopicIsolationProperties.getTopicPrefix(), Arrays.asList(str), TopicPriorityEnum.LOW.name());
                        topicBeanMap.put(TopicPriorityEnum.LOW.name(), topicBean);
                    }else {
                        //批量类的优先级设置为空
                        topicBean = new TopicBean(kafkaTopicIsolationProperties.getTopicPrefix(), Arrays.asList(str, str2), "");
                        topicBeanMap.put(str2, topicBean);
                    }
                }
            }

        }
    }

    @Override
    public String getTopic(String channel, String batchFlag, String templatePriority, Map<String, Map<String, TopicBean>> topicBeansMap) {

        Map<String, TopicBean> topicBeanMap = topicBeansMap.get(channel);
        if (topicBeanMap != null){
            TopicBean topicBean = null;
            if (StringUtils.isNotEmpty(batchFlag)){
                topicBean = topicBeanMap.get(batchFlag);
            }
            if (topicBean == null && StringUtils.isNotEmpty(templatePriority)){
                topicBean = topicBeanMap.get(templatePriority);
            }
            if (topicBean != null){
                return topicBean.buildTopicName();
            }
        }
        return null;
    }
}
