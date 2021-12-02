package cn.com.kun.component.monitor;

public class MonitorTask {

    /**
     * 监控的业务类型
     */
    private String bizType;

    /**
     * 监控任务ID--非必填
     */
    private String monitorTaskId;

    /**
     * 监控逻辑
     */
    private MonitorWorker monitorWorker;

    public MonitorTask(String bizType) {
        this.bizType = bizType;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getMonitorTaskId() {
        return monitorTaskId;
    }

    public void setMonitorTaskId(String monitorTaskId) {
        this.monitorTaskId = monitorTaskId;
    }

    public MonitorWorker getMonitorWorker() {
        return monitorWorker;
    }

    public void setMonitorWorker(MonitorWorker monitorWorker) {
        this.monitorWorker = monitorWorker;
    }
}
