package cn.com.kun.bean.entity;

public class DeadLockDemoDO {


    private Long id;

    private String version;

    private String demoName;

    private String demoKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDemoName() {
        return demoName;
    }

    public void setDemoName(String demoName) {
        this.demoName = demoName;
    }

    public String getDemoKey() {
        return demoKey;
    }

    public void setDemoKey(String demoKey) {
        this.demoKey = demoKey;
    }
}
