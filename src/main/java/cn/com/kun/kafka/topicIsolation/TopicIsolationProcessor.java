package cn.com.kun.kafka.topicIsolation;

import cn.com.kun.kafka.topicIsolation.isolationImpl.TopicIsolationFunction;
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
import org.springframework.util.Assert;

/**
 * 主题拆分处理类
 *
 * author:xuyaokun_kzx
 * date:2023/1/6
 * desc:
*/
@Component
public class TopicIsolationProcessor implements ApplicationContextAware, InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(TopicIsolationProcessor.class);

    private ApplicationContext applicationContext;

    @Autowired
    private KafkaTopicIsolationProperties kafkaTopicIsolationProperties;

    /**
     * 主题拆分实现
     */
    private TopicIsolationFunction topicIsolationFunction;

    @Override
    public void afterPropertiesSet() throws Exception {

        //假如生产者和消费者都没打开，说明该功能没有必要启用
        if (kafkaTopicIsolationProperties.isProducerEnabled() || kafkaTopicIsolationProperties.isConsumerEnabled()){
            //获取实现（根据配置文件决定采用哪种实现，初步设计）
            TopicIsolationFunction topicIsolationFunction = null;
            if (StringUtils.isNotEmpty(kafkaTopicIsolationProperties.getIsolationImplBeanName())){
                topicIsolationFunction = (TopicIsolationFunction) applicationContext.getBean(kafkaTopicIsolationProperties.getIsolationImplBeanName());
            }else {
                //假如配置文件没指定，用组件默认提供的实现
                topicIsolationFunction = applicationContext.getBean(TopicIsolationFunction.class);
            }
            Assert.notNull(topicIsolationFunction, String.format("%s TopicIsolationFunction实现为空", kafkaTopicIsolationProperties.getIsolationImplBeanName()));
            this.topicIsolationFunction = topicIsolationFunction;

            topicIsolationFunction.initTopic(kafkaTopicIsolationProperties, TopicBeanFactory.getTopicBeansMap());
            LOGGER.info("Kafka主题拆分：\n{}", TopicBeanFactory.getAllTopic());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public TopicIsolationFunction getTopicIsolationFunction() {
        return topicIsolationFunction;
    }
}
