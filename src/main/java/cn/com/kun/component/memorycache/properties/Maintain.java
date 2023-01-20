package cn.com.kun.component.memorycache.properties;

import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class Maintain {

    /**
     * 是否存在多套redis
     * 默认为false
     */
    private boolean multiRedis;

    /**
     * 本应用归属的集群名
     */
    private String clusterName = "";

    /**
     * 集群名列表，以逗号分隔
     */
    @Value("#{'${memorycache.clusterList}'.split(',')}")
    private List<String> clusterList;

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
