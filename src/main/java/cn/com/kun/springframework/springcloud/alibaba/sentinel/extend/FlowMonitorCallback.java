package cn.com.kun.springframework.springcloud.alibaba.sentinel.extend;

import cn.com.kun.springframework.springcloud.alibaba.sentinel.vo.MonitorFlag;

@FunctionalInterface
public interface FlowMonitorCallback {

    /**
     * 监控后回调
     * @param monitorFlag
     */
    void monitorCallback(MonitorFlag monitorFlag);

}
