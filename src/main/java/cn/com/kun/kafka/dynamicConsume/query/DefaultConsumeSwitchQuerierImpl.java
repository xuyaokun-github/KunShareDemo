package cn.com.kun.kafka.dynamicConsume.query;

import cn.com.kun.kafka.dynamicConsume.extend.ConsumeSwitchQuerier;
import cn.com.kun.kafka.dynamicConsume.other.CustomTopicOneConsumerService3;
import cn.com.kun.kafka.dynamicConsume.properties.KafkaDynamicConsumeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 默认的开关查询器：支持简单的时段设置
 *
 * author:xuyaokun_kzx
 * date:2023/7/12
 * desc:
*/
@ConditionalOnProperty(name = "kafka.dynamicConsume.startTimeHour")
@Component
public class DefaultConsumeSwitchQuerierImpl implements ConsumeSwitchQuerier {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomTopicOneConsumerService3.class);

    @Autowired
    private KafkaDynamicConsumeProperties dynamicConsumeProperties;

    @PostConstruct
    public void init(){

        LOGGER.info("动态消费组件默认消费开关查询实现启用，消费时间（小时）起始：{} 终止：{}", dynamicConsumeProperties.getStartTimeHour(), dynamicConsumeProperties.getEndTimeHour());
    }

    @Override
    public boolean querySwitch(List<String> topicList) {

        /**
         * 如何向组件返回 消费开关 这个是业务逻辑部分
         * 通常最常见的是 凌晨停止，白天开启
         */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        int hour = calendar.get(Calendar.HOUR);
        if (hour >= dynamicConsumeProperties.getStartTimeHour() && hour < dynamicConsumeProperties.getEndTimeHour()){
            return true;
        }else {
            return false;
        }

    }

}
