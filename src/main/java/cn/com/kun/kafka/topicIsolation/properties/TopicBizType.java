package cn.com.kun.kafka.topicIsolation.properties;

import java.util.List;

public class TopicBizType {

    private String bizKey;

    private List<String> bizValues;
//    private String bizValues;


    public String getBizKey() {
        return bizKey;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    public List<String> getBizValues() {
        return bizValues;
    }

    public void setBizValues(List<String> bizValues) {
        this.bizValues = bizValues;
    }
}
