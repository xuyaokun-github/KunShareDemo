package cn.com.kun.springframework.springkafka.producer;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * 消息回调
 *
 * Created by xuyaokun On 2019/4/24 20:58
 * @desc:
 */
public class MyProducerCallback implements ListenableFutureCallback<SendResult<String, String>> {

    private static Logger LOGGER = LoggerFactory.getLogger(MyProducerCallback.class);

    public MyProducerCallback() {
    }


    @Override
    public void onSuccess(@Nullable SendResult<String, String> result) {

        LOGGER.info("进入消息发送回调方法-成功方法");
        if (result == null) {
            LOGGER.info("进入回调，但消息结果为空");
            return;
        }
        RecordMetadata metadata = result.getRecordMetadata();

    }

    @Override
    public void onFailure(Throwable ex) {
        LOGGER.info("进入消息发送回调-失败方法");
        ex.printStackTrace();
    }
}
