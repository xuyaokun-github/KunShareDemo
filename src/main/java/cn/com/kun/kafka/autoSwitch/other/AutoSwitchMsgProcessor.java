package cn.com.kun.kafka.autoSwitch.other;

import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.kafka.consumer.RecordWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AutoSwitchMsgProcessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(AutoSwitchMsgProcessor.class);

    public boolean doService1(RecordWrapper recordWrapper) {

        //模拟一个耗时
        ThreadUtils.sleep(1000);

        LOGGER.info("AutoSwitchMsgProcessor具体的消费处理：{}", recordWrapper.getRecord().toString());
        return true;
    }

}
