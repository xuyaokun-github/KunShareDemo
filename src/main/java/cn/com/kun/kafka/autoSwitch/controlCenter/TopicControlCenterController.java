package cn.com.kun.kafka.autoSwitch.controlCenter;

import cn.com.kun.kafka.autoSwitch.vo.ControlCenterQryResult;
import cn.com.kun.kafka.autoSwitch.vo.KafkaCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 控制中心，
 * 针对轻量的集群，有没有可能将它通过广播的形式扩散到集群中呢？（去中心化） TODO
 *
 *
 * author:xuyaokun_kzx
 * date:2023/5/4
 * desc:
*/
@RequestMapping("/topic-control-center")
@RestController
public class TopicControlCenterController {

    private final static Logger LOGGER = LoggerFactory.getLogger(TopicControlCenterController.class);

    private Map<String, KafkaCluster> kafkaClusterMap = new HashMap<>();

    private Map<String, String> topicClusterMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() throws IOException {

        /*
            每次启动，必须从db中读取当前集群的状态
            哪些集群可用，哪些集群拥有哪些主题，这些信息必须维护在一个数据库中（要么DB、要么Redis）
            所以这里有很多扩展点，可以面向管理员开发一些便捷功能
         */
        KafkaCluster kafkaCluster = new KafkaCluster("kafkaCluster1", "localhost:9092");
        KafkaCluster kafkaCluster2 = new KafkaCluster("kafkaCluster2", "127.0.0.1:9092");
        kafkaClusterMap.put(kafkaCluster.getName(), kafkaCluster);
        kafkaClusterMap.put(kafkaCluster2.getName(), kafkaCluster2);

        topicClusterMap.put("autoswitch-topic", "kafkaCluster2");
        topicClusterMap.put("autoswitch-topic2", "kafkaCluster2");

    }

    @GetMapping("/clusters")
    public ControlCenterQryResult clusters(){

        ControlCenterQryResult controlCenterQryResult = new ControlCenterQryResult();
        controlCenterQryResult.setKafkaClusterList(kafkaClusterMap.values().stream().collect(Collectors.toList()));
        controlCenterQryResult.setTopicClusterMap(topicClusterMap);
        controlCenterQryResult.setRtnCode("000000");
        return controlCenterQryResult;
    }

    /**
     *
     * @return
     */
    @GetMapping("/update-cluster")
    public String updateCluster(@RequestParam("name") String name, @RequestParam("status") String status){

        //TODO 校验

        kafkaClusterMap.get(name).setStatus(status);
        return "OK";
    }

}
