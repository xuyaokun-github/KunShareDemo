package cn.com.kun.springframework.springcloud.alibaba.sentinel.service;

import cn.com.kun.common.utils.ThreadUtils;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.RESOURCE_NAME_3;
import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.RESOURCE_NAME_4;

/**
 * author:xuyaokun_kzx
 * date:2022/3/1
 * desc:
*/
@Service
public class SentinelDegradeDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SentinelDegradeDemoService.class);

    @PostConstruct
    public void init(){

    }

    public String testDegrade(){

        try (Entry entry = SphU.entry(RESOURCE_NAME_4)) {
            // 被保护的逻辑
            LOGGER.info("开始执行testDegrade");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("结束执行testLimitAndDegrade");
        } catch (BlockException ex) {
            /*
                假如触发的是降级，拿到的将是 DegradeException
             */
            if (ex instanceof DegradeException){
                LOGGER.error("testLimitAndDegrade触发熔断", ex);
            }else {
                LOGGER.error("执行异常", ex);
            }
        }
        // 被保护的逻辑
        LOGGER.info(ThreadUtils.getCurrentInvokeClassAndMethod());

        return ThreadUtils.getCurrentInvokeClassAndMethod() + "执行完毕";
    }

}
