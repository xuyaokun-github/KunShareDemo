package cn.com.kun.quartz.common;

/**
 * 任务定义
 * 对应数据库表 tbl_custom_quartz_job
 * Created by xuyaokun On 2020/6/4 22:45
 * @desc:
 */
public class CustomQuartzJob {

    private int jobId;
    private String jobClass;//任务类
    private String jobName;//任务名
    private String groupName;//组名
    private String jobParam;//任务参数，用json格式存储
    private String triggerName;//触发器名
    private String triggerGroupName;//触发器名
    private String triggerParam;//触发器名
    private String cron;//时间表达式
    private String enabled;//是否启用

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getJobParam() {
        return jobParam;
    }

    public void setJobParam(String jobParam) {
        this.jobParam = jobParam;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroupName() {
        return triggerGroupName;
    }

    public void setTriggerGroupName(String triggerGroupName) {
        this.triggerGroupName = triggerGroupName;
    }

    public String getTriggerParam() {
        return triggerParam;
    }

    public void setTriggerParam(String triggerParam) {
        this.triggerParam = triggerParam;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
}
