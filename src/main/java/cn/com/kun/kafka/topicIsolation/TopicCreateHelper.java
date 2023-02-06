package cn.com.kun.kafka.topicIsolation;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 主题创建辅助类（支持spring-kafka 2.0以上版本）
 *
 * author:xuyaokun_kzx
 * date:2023/2/6
 * desc:
*/
public class TopicCreateHelper {

    public static void main(String[] args) {

        KafkaAdmin kafkaAdmin = null;
        List<String> topicList = null;
        batchCreateTopic(kafkaAdmin, topicList);


        //不是用spring的KafkaAdmin，也是可以的
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        AdminClient adminClient = AdminClient.create(props);
        //亲测成功
        batchCreateTopic(adminClient, topicList);

    }

    /**
     * 不依赖spring-kafka
     *
     * @param adminClient
     * @param topicList
     */
    public static void batchCreateTopic(AdminClient adminClient, List<String> topicList) {

        try {
            // 创建topic
            List<NewTopic> createTopicList = new ArrayList<NewTopic>();
            topicList.forEach(topic->{
                NewTopic newTopic = new NewTopic(topic,16, (short) 1);
                createTopicList.add(newTopic);
            });
            //执行创建的方法
            CreateTopicsResult createTopicsResult = adminClient.createTopics(createTopicList);
            createTopicsResult.all().get();
            System.out.println("创建成功！");
            adminClient.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static void batchCreateTopic(KafkaAdmin kafkaAdmin, List<String> topicList) {

        System.out.println(kafkaAdmin.initialize());
        System.out.println(kafkaAdmin.getConfig().size());

        try {
            //创建连接客户端
            AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfig());
            // 创建topic
            List<NewTopic> createTopicList = new ArrayList<NewTopic>();
            topicList.forEach(topic->{
                NewTopic newTopic = new NewTopic(topic,16, (short) 1);
                createTopicList.add(newTopic);
            });
            //执行创建的方法
            CreateTopicsResult createTopicsResult = adminClient.createTopics(createTopicList);
            createTopicsResult.all().get();
            System.out.println("创建成功！");
            adminClient.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
