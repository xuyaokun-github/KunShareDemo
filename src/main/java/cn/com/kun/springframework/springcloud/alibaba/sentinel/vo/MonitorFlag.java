package cn.com.kun.springframework.springcloud.alibaba.sentinel.vo;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 假设限流TPS是20TPS,取个中间值为15TPS
 * 当流量到了20以上，就到达红线，到了15-20之间就是黄色状态，表示当前负荷较高，可以适当让业务变慢
 * 当在15TPS之下，表示正常状态，无需干预业务执行速度
 *
 * author:xuyaokun_kzx
 * date:2021/10/8
 * desc:
*/
public class MonitorFlag {

    /**
     * 资源名
     */
    private String resource;

    /**
     * 是否到达红线
     */
    private AtomicBoolean redFlag = new AtomicBoolean();

    /**
     * 是否到达黄线
     */
    private AtomicBoolean yellowFlag = new AtomicBoolean();

    /**
     * 是否处于绿色状态
     */
    private AtomicBoolean greenFlag = new AtomicBoolean();

    /**
     * 或者希望有更细级别的用途，可以将具体的监控值填到这里
     */
    private long totalQps;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public AtomicBoolean getRedFlag() {
        return redFlag;
    }

    public void setRedFlag(AtomicBoolean redFlag) {
        this.redFlag = redFlag;
    }

    public AtomicBoolean getYellowFlag() {
        return yellowFlag;
    }

    public void setYellowFlag(AtomicBoolean yellowFlag) {
        this.yellowFlag = yellowFlag;
    }

    public AtomicBoolean getGreenFlag() {
        return greenFlag;
    }

    public void setGreenFlag(AtomicBoolean greenFlag) {
        this.greenFlag = greenFlag;
    }

    public long getTotalQps() {
        return totalQps;
    }

    public void setTotalQps(long totalQps) {
        this.totalQps = totalQps;
    }

    public void markGreen() {
        this.greenFlag.set(true);
        this.yellowFlag.set(false);
        this.redFlag.set(false);
    }


    public void markYellow() {
        this.yellowFlag.set(true);
        this.greenFlag.set(false);
        this.redFlag.set(false);
    }

    public void markRed() {
        this.redFlag.set(true);
        this.greenFlag.set(false);
        this.yellowFlag.set(false);
    }

    @Override
    public String toString() {
        return "MonitorFlag{" +
                "resource='" + resource + '\'' +
                ", redFlag=" + redFlag +
                ", yellowFlag=" + yellowFlag +
                ", greenFlag=" + greenFlag +
                ", totalQps=" + totalQps +
                '}';
    }
}
