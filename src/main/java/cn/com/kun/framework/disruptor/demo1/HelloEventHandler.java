package cn.com.kun.framework.disruptor.demo1;

import com.lmax.disruptor.EventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消费者
 *
 * author:xuyaokun_kzx
 * date:2023/2/10
 * desc:
*/
public class HelloEventHandler implements EventHandler<MessageModel> {

    private final static Logger log = LoggerFactory.getLogger(HelloEventHandler.class);

    @Override
    public void onEvent(MessageModel event, long sequence, boolean endOfBatch) {
        try {
            //这里停止1000ms是为了确定消费消息是异步的
            Thread.sleep(1000);
            log.info("消费者处理消息开始");
            if (event != null) {
                log.info("消费者消费的信息是：{}", event);
            }
        } catch (Exception e) {
            log.info("消费者处理消息失败");
        }
        log.info("消费者处理消息结束");
    }
}
