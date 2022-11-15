package cn.com.kun.component.monitor.model;

import cn.com.kun.component.monitor.worker.MonitorWorker;

import java.util.concurrent.TimeUnit;

/**
 * 监控任务（定时检测任务）
 *
 * author:xuyaokun_kzx
 * date:2022/11/4
 * desc:
 * 1.监控任务应该给一个执行频率（方便判断是否应开始执行监控逻辑，这个逻辑不需要很精准）已完成
 * 2.有一类监控任务，只能在某天的某个时间段运行的（这种其实就是cron表达式）后面可继续扩展 TODO
 *
*/
public class MonitorTask {

    /**
     * 监控的业务类型
     */
    private String bizType;

    /**
     * 监控任务ID--非必填
     * 第一类监控场景，是基于ID进行监控的
     * （例如每个批处理任务启动后，都需要监控，一个批处理任务就会对应一个MonitorTask，任务执行完，监控任务也要销毁）
     * 第二类监控场景，永久执行，无需停止
     * （例如监控内存是否到90%，到了就发出告警提示）
     */
    private String monitorTaskId;

    /**
     * 监控逻辑
     */
    private MonitorWorker monitorWorker;

    /**
     * 配合TimeUnit一起用
     */
    private long timePeriod = 10;

    /**
     * 默认情况为 1秒一次
     */
    private TimeUnit timeUnit = TimeUnit.SECONDS;

    /**
     * 上一次执行时间
     */
    private long lastExecTimeMillis = 0;

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

    public long getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(long timePeriod) {
        this.timePeriod = timePeriod;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public long getLastExecTimeMillis() {
        return lastExecTimeMillis;
    }

    public void setLastExecTimeMillis(long lastExecTimeMillis) {
        this.lastExecTimeMillis = lastExecTimeMillis;
    }
}
