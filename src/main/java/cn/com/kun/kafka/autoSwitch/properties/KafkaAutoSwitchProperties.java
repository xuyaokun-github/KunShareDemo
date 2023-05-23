package cn.com.kun.kafka.autoSwitch.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 *
 * author:xuyaokun_kzx
 * date:2022/12/29
 * desc:
*/
@Component
@ConfigurationProperties(prefix ="kafka.autoswitch")
public class KafkaAutoSwitchProperties implements Serializable {

    private boolean consumerEnabled;

    private boolean producerEnabled;

    /**
     * 控制中心访问地址
     */
    private String controlCenterUrl;

    /**
     * 目标集群（优先集群、倾向集群）
     * 当目标集群不可用的时候，会采用当前其他可用集群
     *
     */
    private String targetCluster;

    public boolean isConsumerEnabled() {
        return consumerEnabled;
    }

    public void setConsumerEnabled(boolean consumerEnabled) {
        this.consumerEnabled = consumerEnabled;
    }

    public boolean isProducerEnabled() {
        return producerEnabled;
    }

    public void setProducerEnabled(boolean producerEnabled) {
        this.producerEnabled = producerEnabled;
    }

    public String getControlCenterUrl() {
        return controlCenterUrl;
    }

    public void setControlCenterUrl(String controlCenterUrl) {
        this.controlCenterUrl = controlCenterUrl;
    }

    public String getTargetCluster() {
        return targetCluster;
    }

    public void setTargetCluster(String targetCluster) {
        this.targetCluster = targetCluster;
    }
}
