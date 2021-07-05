package cn.com.kun.component.hystrixextend;

public class LimitItem {

    /**
     * 区分不同的子场景的关键字
     */
    private String name;

    /**
     * 限流值
     */
    private String rate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
