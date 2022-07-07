package cn.com.kun.component.memorycache.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "memorycache")
public class MemoryCacheProperties {

    private long detectThreadSleepTime;

    /**
     * 全局开关
     * 设置为false,禁用MemoryCache功能
     */
    private boolean enabled;

    /**
     * 是否存在多套redis
     */
    private boolean multiRedis;

    /**
     * 本应用归属的集群名
     */
    private String clusterName;

    /**
     * 集群名列表，以逗号分隔
     */
    @Value("#{'${memorycache.clusterList}'.split(',')}")
    private List<String> clusterList;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public long getDetectThreadSleepTime() {
        return detectThreadSleepTime;
    }

    public void setDetectThreadSleepTime(long detectThreadSleepTime) {
        this.detectThreadSleepTime = detectThreadSleepTime;
    }

    public boolean isMultiRedis() {
        return multiRedis;
    }

    public void setMultiRedis(boolean multiRedis) {
        this.multiRedis = multiRedis;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public List<String> getClusterList() {
        return clusterList;
    }

    public void setClusterList(List<String> clusterList) {
        this.clusterList = clusterList;
    }
}
