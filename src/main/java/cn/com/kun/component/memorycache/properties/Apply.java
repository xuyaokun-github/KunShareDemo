package cn.com.kun.component.memorycache.properties;

public class Apply {


    private String caffeineCacheManagerName;

    /**
     * 检测线程睡眠时间
     */
    private long detectThreadSleepTime = 60000;

    public String getCaffeineCacheManagerName() {
        return caffeineCacheManagerName;
    }

    public void setCaffeineCacheManagerName(String caffeineCacheManagerName) {
        this.caffeineCacheManagerName = caffeineCacheManagerName;
    }

    public long getDetectThreadSleepTime() {
        return detectThreadSleepTime;
    }

    public void setDetectThreadSleepTime(long detectThreadSleepTime) {
        this.detectThreadSleepTime = detectThreadSleepTime;
    }
}
