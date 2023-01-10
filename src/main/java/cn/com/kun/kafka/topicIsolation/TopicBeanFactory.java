package cn.com.kun.kafka.topicIsolation;

import cn.com.kun.kafka.topicIsolation.bean.TopicBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TopicBeanFactory {


    /**
     * 假如用 实时支持优先级-批量无优先级 模式
     * 第一层的key:业务字段1 即下游渠道
     * 第二层的key:业务字段2 是否批量 或者 优先级
     */
    private static Map<String, Map<String, TopicBean>> topicBeansMap = new ConcurrentHashMap<>();

    public static Map<String, Map<String, TopicBean>> getTopicBeansMap() {

        return topicBeansMap;
    }

    public static String getAllTopic(){

        StringBuilder builder = new StringBuilder();
        topicBeansMap.forEach((k,v)->{
            v.forEach((k1,v1)->{
                builder.append(v1.buildTopicName() + "\n");
            });
        });
        String allTopic = builder.toString();
        return allTopic;
    }


}
