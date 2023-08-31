package cn.com.kun.kafka.dataStatMonitor.stat.interceptor;

import cn.com.kun.kafka.dataStatMonitor.stat.counter.TopicDataStatCounter;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static cn.com.kun.kafka.dataStatMonitor.constants.DataStatConstant.MSG_TYPE_HEADER_KEY_NAME;

/**
 * 数据统计-消费者端
 *
 * author:xuyaokun_kzx
 * date:2023/8/21
 * desc:
*/
public class DataStatConsumerInterceptor implements ConsumerInterceptor<String, String> {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataStatConsumerInterceptor.class);

    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {

        LOGGER.info("进入onConsume方法");
        if (records.count() > 0) {

            records.forEach(record ->{
                //消息类型
                String msgType = null;
                //解析header头
                if (record.headers() != null){
                    Header[] headers = record.headers().toArray();
                    for (Header header : headers){
                        if (MSG_TYPE_HEADER_KEY_NAME.equals(header.key())){
                            msgType = new String(header.value(), StandardCharsets.UTF_8);
                            LOGGER.info("消费者拦截器解析到消息类型：{}", msgType);
                            break;
                        }
                    }
                }
                if (msgType != null && msgType.length() > 0){
                    TopicDataStatCounter.subtract(record.topic(), msgType);
                }
            });
        }

        return records;
    }


    @Override
    public void onCommit(Map<TopicPartition, OffsetAndMetadata> offsets) {

    }


    @Override
    public void close() {

    }


    @Override
    public void configure(Map<String, ?> configs) {

    }
}

