package cn.com.kun.kafka.autoSwitch.enums;

public enum KafkaClusterStatusEnum {

    /**
     */
    ENABLED("1"),
    DISABLED("0"),
    LOW("LOW");

    /**
     * 表名
     */
    public String value;

    KafkaClusterStatusEnum(String value) {
        this.value = value;
    }

}
