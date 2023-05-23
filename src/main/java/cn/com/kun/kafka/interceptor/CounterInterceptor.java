package cn.com.kun.kafka.interceptor;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

public class CounterInterceptor implements ProducerInterceptor<String, String>  {

    private int errorCounter = 0;

    private int successCounter = 0;


    /**
     *  发送消息回调
     * @return
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        return record;
    }

    /**
     *  发送完成回调
     */
    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
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
