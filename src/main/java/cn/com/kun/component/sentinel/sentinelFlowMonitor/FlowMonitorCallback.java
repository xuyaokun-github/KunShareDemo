package cn.com.kun.component.sentinel.sentinelFlowMonitor;

import cn.com.kun.component.sentinel.sentinelFlowMonitor.vo.FlowMonitorRes;

@FunctionalInterface
public interface FlowMonitorCallback {

    /**
     * 监控后回调
     * @param flowMonitorRes
     */
    void monitorCallback(FlowMonitorRes flowMonitorRes);

}
