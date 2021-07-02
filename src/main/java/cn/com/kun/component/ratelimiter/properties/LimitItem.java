package cn.com.kun.component.ratelimiter.properties;

public class LimitItem {

    /**
     * 区分不同的子场景的关键字
     */
    private String name;

    /**
     * 限流值
     */
    private Double rate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
