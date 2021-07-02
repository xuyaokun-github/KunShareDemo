package cn.com.kun.springframework.springcloud.hystrix.hystrixExtend;

import java.util.List;

public class BusinessSceneLimit {

    /**
     * 每个业务场景，有一个默认的限流值
     */
    private String defaultRate;

    /**
     * 一个业务场景下，有多种情况
     * 根据某一个业务字段区分这种情况
     * 例如发送消息是一个业务场景，而根据哪个渠道发送，这个渠道就可以作为业务字段进行区分
     * @return
     */
    private List<LimitItem> limitItem;

    public String getDefaultRate() {
        return defaultRate;
    }

    public void setDefaultRate(String defaultRate) {
        this.defaultRate = defaultRate;
    }

    public List<LimitItem> getLimitItem() {
        return limitItem;
    }

    public void setLimitItem(List<LimitItem> limitItem) {
        this.limitItem = limitItem;
    }

    public static class LimitItem {

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

}
