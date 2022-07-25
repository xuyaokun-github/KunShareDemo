package cn.com.kun.component.sentinel.sentinelFlowMonitor;

import cn.com.kun.component.sentinel.sentinelFlowMonitor.vo.FlowMonitorRes;

public interface StoreMonitorResultService {

    /**
     * 保存监控结果
     * @param flowMonitorRes
     */
    void storeMonitorResult(FlowMonitorRes flowMonitorRes);

}
