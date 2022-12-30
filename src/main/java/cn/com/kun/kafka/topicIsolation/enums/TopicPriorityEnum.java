package cn.com.kun.kafka.topicIsolation.enums;

public enum TopicPriorityEnum {

    /**
     */
    HIGH("HIGH"),
    MIDDLE("MIDDLE"),
    LOW("LOW");

    /**
     * 表名
     */
    public String name;

    TopicPriorityEnum(String name) {
        this.name = name;
    }

}
