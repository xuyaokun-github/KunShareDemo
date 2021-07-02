package cn.com.kun.component.ratelimiter.properties;

public class IPLimit {

    private String ip;

    private Double ipRate;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Double getIpRate() {
        return ipRate;
    }

    public void setIpRate(Double ipRate) {
        this.ipRate = ipRate;
    }
}
