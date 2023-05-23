package cn.com.kun.kafka.autoSwitch.vo;

import java.util.List;
import java.util.Map;

/**
 * 集群信息查询结果
 *
 * author:xuyaokun_kzx
 * date:2023/5/5
 * desc:
*/
public class ControlCenterQryResult {

    /**
     * 返回码
     */
    private String rtnCode;

    /**
     * 集群列表
     */
    private List<KafkaCluster> kafkaClusterList;

    private Map<String, String> topicClusterMap;

    public List<KafkaCluster> getKafkaClusterList() {
        return kafkaClusterList;
    }

    public void setKafkaClusterList(List<KafkaCluster> kafkaClusterList) {
        this.kafkaClusterList = kafkaClusterList;
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public Map<String, String> getTopicClusterMap() {
        return topicClusterMap;
    }

    public void setTopicClusterMap(Map<String, String> topicClusterMap) {
        this.topicClusterMap = topicClusterMap;
    }

    public boolean isSuccess() {
        return "000000".equals(this.rtnCode);
    }

}
