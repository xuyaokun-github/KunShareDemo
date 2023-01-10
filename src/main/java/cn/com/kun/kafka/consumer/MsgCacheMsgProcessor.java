package cn.com.kun.kafka.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MsgCacheMsgProcessor {

    private final static Logger LOGGER = LoggerFactory.getLogger(MsgCacheMsgProcessor.class);

    public boolean doService1(RecordWrapper recordWrapper) {

        LOGGER.info("具体的消费处理：{}", recordWrapper.getRecord().toString());
        return true;
    }

}
