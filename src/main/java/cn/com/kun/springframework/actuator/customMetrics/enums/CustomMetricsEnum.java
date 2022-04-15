package cn.com.kun.springframework.actuator.customMetrics.enums;


/**
 * 业务监控枚举
 * 为了方便可临时定义成枚举类，
 * 假如经常变，可以考虑持久化到数据库，启动时查库进行初始化
 * 通常业务监控不会经常变，先用枚举够用了
 *
 * author:xuyaokun_kzx
 * date:2021/11/24
 * desc:
*/
public enum CustomMetricsEnum {

    /**
     * 定义不同的业务监控场景
     */
    ACTIVITY_REJECT_STAT("ACTIVITY_REJECT_STAT", "activityId"),
    MYCUSTOMMETRICS_ONE("MYCUSTOMMETRICS_ONE", "successFlag,type,oper"),
    MYTWO("MYTWO", "successFlag,type,oper,time");

    CustomMetricsEnum(String counterName, String tagList) {
        this.counterName = counterName;
        this.tagList = tagList;
    }

    public String counterName;

    public String tagList;

    public String getCounterName() {
        return counterName;
    }

    public void setCounterName(String counterName) {
        this.counterName = counterName;
    }

    public String getTagList() {
        return tagList;
    }

    public void setTagList(String tagList) {
        this.tagList = tagList;
    }


}
