package cn.com.kun.kafka.topicIsolation.producer;

import cn.com.kun.kafka.topicIsolation.TopicBeanFactory;
import cn.com.kun.kafka.topicIsolation.TopicIsolationProcessor;
import cn.com.kun.kafka.topicIsolation.properties.KafkaTopicIsolationProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 生产者端使用
 *
 * author:xuyaokun_kzx
 * date:2022/12/30
 * desc:
*/
@Component
public class ProducerTopicProcessor implements ApplicationContextAware, InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProducerTopicProcessor.class);

    private ApplicationContext applicationContext;

    @Autowired
    private KafkaTopicIsolationProperties kafkaTopicIsolationProperties;

    @Autowired
    private TopicIsolationProcessor topicIsolationProcessor;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /**
     * 根据具体的业务字段，得到目标主题
     * 第一个字段：渠道字段
     * 第二个字段：是否批量
     * 第三个字段：模板优先级
     *
     * @return
     */
    public String getTopic(String bizField1, String bizField2, String templatePriority){

        if (!kafkaTopicIsolationProperties.isProducerEnabled()){
            throw new RuntimeException("Kafka主题拆分组件，生产者未启用");
        }

        String topicName = topicIsolationProcessor.getTopicIsolationFunction().getTopic(bizField1, bizField2, templatePriority, TopicBeanFactory.getTopicBeansMap());
        if (StringUtils.isNotEmpty(topicName)){
            return topicName;
        }
        //默认主题
        return kafkaTopicIsolationProperties.getDefaultTopic();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
