package cn.com.kun.springframework.springcloud.alibaba.sentinel.demo.kafkademo;

import cn.com.kun.common.utils.DateUtils;
import cn.com.kun.component.kafkaConsumerSpeed.KafkaConsumerThreadConstants;
import cn.com.kun.component.kafkaConsumerSpeed.KafkaConsumerThreadManager;
import cn.com.kun.component.sentinel.sentinelFlowMonitor.SentinelFlowMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 这个类模拟一个消费者线程
 *
 * author:xuyaokun_kzx
 * date:2021/10/8
 * desc:
*/
@Service
public class KafkaSentinelThreadDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaSentinelThreadDemoService.class);

    @Autowired
    KafkaConsumerThreadManager kafkaConsumerThreadManager;

    @Autowired
    private SentinelFlowMonitor sentinelFlowMonitor;


//    @PostConstruct
    public void init(){

        new Thread(()->{

            while (true){
                try {
                    LOGGER.info("我是kafka消费线程,{}", DateUtils.now());

                    //正常情况下，拉消息是一直拉，没有睡眠 或者是睡眠1秒
                    //假如触发了黄线或者红线
                    //就开始sleep
                    kafkaConsumerThreadManager.await(KafkaConsumerThreadConstants.CONSUMER_THREAD_TYPE_MIDDLE);

                }catch (Exception e){
                    LOGGER.error("kafka消费线程异常", e);
                }
            }

        }).start();
    }

}
