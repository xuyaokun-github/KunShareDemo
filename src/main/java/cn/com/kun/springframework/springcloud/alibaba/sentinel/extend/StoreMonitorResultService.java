package cn.com.kun.springframework.springcloud.alibaba.sentinel.extend;

import cn.com.kun.springframework.springcloud.alibaba.sentinel.vo.FlowMonitorRes;

public interface StoreMonitorResultService {

    /**
     * 保存监控结果
     * @param flowMonitorRes
     */
    void storeMonitorResult(FlowMonitorRes flowMonitorRes);

}
