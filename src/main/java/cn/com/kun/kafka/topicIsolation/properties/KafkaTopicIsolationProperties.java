package cn.com.kun.kafka.topicIsolation.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

/**
 *
 * author:xuyaokun_kzx
 * date:2022/12/29
 * desc:
*/
@Component
@ConfigurationProperties(prefix ="kafka.topicisolation")
public class KafkaTopicIsolationProperties implements Serializable {

    private String topicPrefix;

    private List<TopicBizType> bizTypes;

    private String defaultTopic;

    /**
     * 扩展点：主题隔离实现类
     * 可随意更换扩展点
     *
     */
    private String isolationImplBeanName;

    public String getTopicPrefix() {
        return topicPrefix;
    }

    public void setTopicPrefix(String topicPrefix) {
        this.topicPrefix = topicPrefix;
    }

    public List<TopicBizType> getBizTypes() {
        return bizTypes;
    }

    public void setBizTypes(List<TopicBizType> bizTypes) {
        this.bizTypes = bizTypes;
    }

    public String getDefaultTopic() {
        return defaultTopic;
    }

    public void setDefaultTopic(String defaultTopic) {
        this.defaultTopic = defaultTopic;
    }

    public String getIsolationImplBeanName() {
        return isolationImplBeanName;
    }

    public void setIsolationImplBeanName(String isolationImplBeanName) {
        this.isolationImplBeanName = isolationImplBeanName;
    }
}
