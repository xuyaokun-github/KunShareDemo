package cn.com.kun.springframework.springcloud.alibaba.sentinel.extend;

import cn.com.kun.springframework.springcloud.alibaba.sentinel.vo.FlowMonitorRes;

@FunctionalInterface
public interface FlowMonitorCallback {

    /**
     * 监控后回调
     * @param flowMonitorRes
     */
    void monitorCallback(FlowMonitorRes flowMonitorRes);

}
