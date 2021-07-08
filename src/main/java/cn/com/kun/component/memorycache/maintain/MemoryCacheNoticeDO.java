package cn.com.kun.component.memorycache.maintain;

public class MemoryCacheNoticeDO {

    private Long id;

    private String configName;

    private String bizKey;

    private String updateTimemillis;

    private String clusterName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getBizKey() {
        return bizKey;
    }

    public void setBizKey(String bizKey) {
        this.bizKey = bizKey;
    }

    public String getUpdateTimemillis() {
        return updateTimemillis;
    }

    public void setUpdateTimemillis(String updateTimemillis) {
        this.updateTimemillis = updateTimemillis;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
