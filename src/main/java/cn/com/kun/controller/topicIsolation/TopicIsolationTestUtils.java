package cn.com.kun.controller.topicIsolation;

import cn.com.kun.kafka.topicIsolation.bean.TopicBean;
import cn.com.kun.kafka.topicIsolation.enums.TopicPriorityEnum;

import java.util.Arrays;

public class TopicIsolationTestUtils {

    public static void main(String[] args) {

        TopicBean topicBean = new TopicBean("MSG_CACHE", Arrays.asList("SM01", "BATCH"), "HIGH");
        System.out.println(topicBean.buildTopicName());
        TopicBean topicBean2 = new TopicBean("MSG_CACHE", Arrays.asList("PM01", "BATCH"), "HIGH");
        System.out.println(topicBean2.buildTopicName());
        TopicBean topicBean3 = new TopicBean("MSG_CACHE", Arrays.asList("PM01", "BATCH"), "");
        System.out.println(topicBean3.buildTopicName());
        TopicBean topicBean4 = new TopicBean("MSG_CACHE", Arrays.asList("PM01"), "HIGH");
        System.out.println(topicBean4.buildTopicName());


        System.out.println(TopicPriorityEnum.HIGH.name);

    }


}
