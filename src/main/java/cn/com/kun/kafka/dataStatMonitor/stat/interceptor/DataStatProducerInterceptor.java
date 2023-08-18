package cn.com.kun.kafka.dataStatMonitor.stat.interceptor;

import cn.com.kun.kafka.dataStatMonitor.stat.TopicDataStatProcessor;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static cn.com.kun.kafka.dataStatMonitor.constants.DataStatConstant.MSG_TYPE_HEADER_KEY_NAME;

public class DataStatProducerInterceptor implements ProducerInterceptor<String, String>  {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataStatProducerInterceptor.class);

    private int errorCounter = 0;

    private int successCounter = 0;


    /**
     *  发送消息回调
     * @return
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {

        String msgType = null;
        Header[] headers = record.headers().toArray();
        for (Header header : headers){
            if (MSG_TYPE_HEADER_KEY_NAME.equals(header.key())){
                msgType = new String(header.value(), StandardCharsets.UTF_8);
                LOGGER.info("解析到消息类型：{}", msgType);
                break;
            }
        }
        if (msgType != null && msgType.length() > 0){
            //进行统计
            TopicDataStatProcessor.add(record.topic(), msgType);
        }

        LOGGER.info("调用onSend方法");
        return record;
    }

    /**
     *  发送完成回调
     */
    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        LOGGER.info("调用onAcknowledgement方法");
        // 统计成功和失败的次数
        if (e == null) {
            successCounter++;
        } else {
            errorCounter++;
        }
    }

    /**
     * 关闭方法
     */
    @Override
    public void close() {
        // 保存结果
//        System.out.println("Successful sent: " + successCounter);
//        System.out.println("Failed sent: " + errorCounter);
    }

    /**
     * 配置完成方法
     */
    @Override
    public void configure(Map<String, ?> map) {
//        System.out.println("111222");
    }
}
