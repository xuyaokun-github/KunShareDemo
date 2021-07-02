package cn.com.kun.component.ratelimiter.properties;

import java.util.List;

public class BizSceneLimit {

    /**
     * 每个业务场景，有一个默认的限流值
     */
    private Double defaultRate;

    /**
     * 一个业务场景下，有多种情况
     * 根据某一个业务字段区分这种情况
     * 例如发送消息是一个业务场景，而根据哪个渠道发送，这个渠道就可以作为业务字段进行区分
     * @return
     */
    private List<LimitItem> limitItem;

    public Double getDefaultRate() {
        return defaultRate;
    }

    public void setDefaultRate(Double defaultRate) {
        this.defaultRate = defaultRate;
    }

    public List<LimitItem> getLimitItem() {
        return limitItem;
    }

    public void setLimitItem(List<LimitItem> limitItem) {
        this.limitItem = limitItem;
    }
}
