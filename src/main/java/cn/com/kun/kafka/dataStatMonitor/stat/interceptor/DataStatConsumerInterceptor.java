package cn.com.kun.kafka.dataStatMonitor.stat.interceptor;

import cn.com.kun.kafka.dataStatMonitor.stat.TopicDataStatProcessor;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DataStatConsumerInterceptor implements ConsumerInterceptor<String, String> {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataStatConsumerInterceptor.class);

    @Override
    public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> records) {

        LOGGER.info("进入onConsume方法");
        if (records.count() > 0) {

            records.forEach(record ->{
                //解析header头 TODO

                TopicDataStatProcessor.subtract("", "");
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

