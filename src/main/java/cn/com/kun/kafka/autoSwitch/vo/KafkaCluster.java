package cn.com.kun.kafka.autoSwitch.vo;

/**
 *
 * author:xuyaokun_kzx
 * date:2023/5/5
 * desc:
*/
public class KafkaCluster {

    /**
     * 集群名
     */
    private String name;

    /**
     * ip地址
     * 假如涉及证书，多个集群都应该用同一套证书
     */
    private String address;

    /**
     * 集群状态：
     * 0：不可用
     * 1：可用
     * 默认为可用
     *
     */
    private String status = "1";

    public KafkaCluster() {
    }

    public KafkaCluster(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "KafkaCluster{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
