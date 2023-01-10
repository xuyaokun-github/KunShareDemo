package cn.com.kun.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class RecordWrapper {

    private CountDownLatch countDownLatch;
    private AtomicBoolean isAllSuccess;

    /**
     * 具体消息内容
     */
    private ConsumerRecord<String, String> record;

    /**
     * 该条消息是否处理成功
     */
    private boolean isSuccess;

    public RecordWrapper(ConsumerRecord<String, String> record) {
        this.record = record;
        this.isSuccess = false;
    }

    public ConsumerRecord<String, String> getRecord() {
        return record;
    }

    public void setRecord(ConsumerRecord<String, String> record) {
        this.record = record;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }



    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public AtomicBoolean getIsAllSuccess() {
        return isAllSuccess;
    }

    public void setIsAllSuccess(AtomicBoolean isAllSuccess) {
        this.isAllSuccess = isAllSuccess;
    }
}
