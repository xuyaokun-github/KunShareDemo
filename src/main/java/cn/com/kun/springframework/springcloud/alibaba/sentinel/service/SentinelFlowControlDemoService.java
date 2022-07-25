package cn.com.kun.springframework.springcloud.alibaba.sentinel.service;

import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.component.sentinel.sentinelCompensate.SentinelFlowControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SentinelFlowControlDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SentinelFlowControlDemoService.class);

    int a = 1;

    /**
     * 简单限流(注解方式，直接能用，无法做额外的配置)
     */
    @SentinelFlowControl(value = "SentinelFlowControlDemoService", bizName = "查询用户体系", pauseTime = 1000)
    public String test(){
        // 被保护的逻辑
        LOGGER.info("正在查询用户体系");
        int b = 3/a;
        return ThreadUtils.getCurrentInvokeClassAndMethod() + "执行完毕";
    }

    public void makeException(){
        a = 0;
    }

}
