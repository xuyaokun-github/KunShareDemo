package cn.com.kun.component.ratelimiter.properties;

public class ApiLimit {

    /**
     * 接口地址，可以写具体的，也可以只写一部分
     * 在判断的时候，通过requestUri.contains(api)判断
     *
     */
    private String api;

    /**
     * 接口对应的限流值（每秒多少个请求）
     * 为什么定义成Double值？因为RateLimiter限流器构造器参数是Double类型
     */
    private Double apiRate;

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public Double getApiRate() {
        return apiRate;
    }

    public void setApiRate(Double apiRate) {
        this.apiRate = apiRate;
    }
}
