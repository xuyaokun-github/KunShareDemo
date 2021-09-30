package cn.com.kun.springframework.springcloud.alibaba.sentinel.kafkademo;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.extend.FlowMonitorProcessor;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.vo.MonitorFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.RESOURCE_NAME;

@Service
public class KafkaSentinelThreadDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaSentinelThreadDemoService.class);

    /**
     * 要监控哪些资源呢？
     */
    private String resourceName = "";

    @Autowired
    private FlowMonitorProcessor flowMonitorProcessor;

    @PostConstruct
    public void init(){

        new Thread(()->{

            while (true){

                try {
                    LOGGER.info("我是kafka消费线程,{}", DateUtils.now());
                    MonitorFlag monitorFlag = flowMonitorProcessor.getFlowMonitorFlag(RESOURCE_NAME);
                    LOGGER.info("monitorFlag：{}", JacksonUtils.toJSONString(monitorFlag));
                    if (monitorFlag.getRedFlag().get()){
                        Thread.sleep(10000);
                    }else {
                        Thread.sleep(1000);
                    }
                    //正常情况下，拉消息是一直拉，没有睡眠 或者是睡眠1秒
                    //假如触发了黄线或者红线
                    //就开始sleep
                }catch (Exception e){
                    e.printStackTrace();
                }

            }



        }).start();
    }

}
