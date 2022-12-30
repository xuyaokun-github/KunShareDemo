package cn.com.kun.kafka.topicIsolation;

import cn.com.kun.kafka.topicIsolation.bean.TopicBean;
import cn.com.kun.kafka.topicIsolation.isolationImpl.TopicIsolationFunction;
import cn.com.kun.kafka.topicIsolation.properties.KafkaTopicIsolationProperties;
import cn.com.kun.springframework.springmvc.SpringMvcDemoController;
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 生产者端使用
 *
 * author:xuyaokun_kzx
 * date:2022/12/30
 * desc:
*/
@Component
public class ProducerTopicProcessor implements ApplicationContextAware, InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringMvcDemoController.class);

    private ApplicationContext applicationContext;

    @Autowired
    private KafkaTopicIsolationProperties kafkaTopicIsolationProperties;


    private String DEFAULI_TOPIC = null;

    /**
     * 假如用 实时支持优先级-批量无优先级 模式
     * 第一层的key:业务字段1 即下游渠道
     * 第二层的key:业务字段2 是否批量 或者 优先级
     */
    private Map<String, Map<String, TopicBean>> topicBeansMap = new ConcurrentHashMap<>();

    TopicIsolationFunction topicIsolationFunction;

    @Override
    public void afterPropertiesSet() throws Exception {

        //默认主题
        DEFAULI_TOPIC = kafkaTopicIsolationProperties.getDefaultTopic();
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
        topicIsolationFunction.initTopic(kafkaTopicIsolationProperties, topicBeansMap);
        LOGGER.info("Kafka主题拆分：\n{}", getAllTopic());
    }

    public String getAllTopic(){

        StringBuilder builder = new StringBuilder();
        topicBeansMap.forEach((k,v)->{
            v.forEach((k1,v1)->{
                builder.append(v1.buildTopicName() + "\n");
            });
        });
        String allTopic = builder.toString();
        return allTopic;
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

        String topicName = topicIsolationFunction.getTopic(bizField1, bizField2, templatePriority, topicBeansMap);
        if (StringUtils.isNotEmpty(topicName)){
            return topicName;
        }
        return DEFAULI_TOPIC;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
