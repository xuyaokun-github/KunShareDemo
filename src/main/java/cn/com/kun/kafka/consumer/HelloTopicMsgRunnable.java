package cn.com.kun.kafka.consumer;

/**
 * 线程任务
 * author:xuyaokun_kzx
 * date:2021/7/16
 * desc:
*/
public class HelloTopicMsgRunnable implements Runnable{

    private MsgCacheMsgProcessor processor;

    private RecordWrapper recordWrapper;

    public HelloTopicMsgRunnable(MsgCacheMsgProcessor msgCacheMsgProcessor, RecordWrapper record) {
        this.processor = msgCacheMsgProcessor;
        this.recordWrapper = record;
    }

    @Override
    public void run() {

        /*
            方案1：
            直接用当前线程处理消息，处理失败的才入库，因为重试会增加处理总时长
         */
        boolean isSuccess = processor.doService1(recordWrapper);
        //boolean isSuccess = processor.doService2(recordWrapper);
        if (!isSuccess){
            //写补偿表失败了,打上标记
            recordWrapper.getIsAllSuccess().set(false);
        }else {
            //消息已经消费成功
        }
        //计数器减一
        recordWrapper.getCountDownLatch().countDown();
    }



}
