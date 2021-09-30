package cn.com.kun.springframework.springcloud.alibaba.sentinel.vo;

import java.util.concurrent.atomic.AtomicBoolean;

public class MonitorFlag {

    /**
     * 是否到达红线
     */
    private AtomicBoolean redFlag = new AtomicBoolean();

    /**
     *
     */
    private AtomicBoolean yellowFlag = new AtomicBoolean();

    /**
     *
     */
    private AtomicBoolean greenFlag = new AtomicBoolean();

    /**
     * 或者希望有更细级别的用途，可以将具体的监控值填到这里
     */
    private long totalQps;

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
}
