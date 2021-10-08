package cn.com.kun.springframework.springcloud.alibaba.sentinel.demo.kafkademo;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.extend.FlowMonitorProcessor;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.vo.MonitorFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.RESOURCE_NAME;

/**
 * Kafka消费线程管理器
 * 作用：通过让Kafka线程睡眠，从而控制Kafka线程消费速度
 *
 * author:xuyaokun_kzx
 * date:2021/10/8
 * desc:
*/
@Component
public class KafkaConsumerThreadManager {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerThreadManager.class);

    @Autowired
    private FlowMonitorProcessor flowMonitorProcessor;

    /**
     * 开始等待
     * 假如需要控制消费线程速度，可以在一批消息执行完之后调用该方法
     * 睡眠N秒，然后开始下一次拉取
     */
    public void await() throws InterruptedException {

        /*
         * 需要监听哪些资源，根据业务决定
         */
        MonitorFlag monitorFlag = flowMonitorProcessor.getFlowMonitorFlag(RESOURCE_NAME);
        LOGGER.info("monitorFlag：{}", JacksonUtils.toJSONString(monitorFlag));
        if (monitorFlag != null){
            Thread.sleep(calculateWaitTime(monitorFlag));
        }

    }

    private long calculateWaitTime(MonitorFlag monitorFlag){

        long time = 0L;
        /*
         * 决定睡眠多久，可以灵活设置
         */
        if (monitorFlag.getRedFlag().get()){
            time = 10000;
        }else if (monitorFlag.getYellowFlag().get()){
            time = 5000;
        } else {
            time = 1000;
        }

        //也可以根据具体的QPS值决定该睡多久,可以制定一个公式，灵活判断
        monitorFlag.getTotalQps();

        return time;
    }

}
