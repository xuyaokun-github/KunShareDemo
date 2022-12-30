package cn.com.kun.springframework.springkafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class HelloTopicMsgProcessor {

    private static Logger LOGGER = LoggerFactory.getLogger(HelloTopicMsgProcessor.class);

    /**
     * 处理消息
     * @param record
     * @param ack
     */
    public void process(ConsumerRecord record, Acknowledgment ack) {

        String resultVo = (String) record.value();
        LOGGER.info("准备手动提交");
        //进行确认
        if (true){
//            ack.acknowledge();
        }
        /*
            假如不确认，下一次重启，还是会访问到
            （前提是没有其他新来的消息被成功确认消息，
            因为假如有新的消息在它之后被确认，会导致整体偏移量移位，
            这样旧的这一条就无法再次被访问，这种情况是会出现的）
         */

        //所以对客户端消费者来说，处理消息变得很关键
        //假如出异常了，导致没确认，很可能下一次这条消息不会再被访问
        //所以最佳实践是，假如消息的业务处理异常了，考虑做补偿或者是否让消息重新入队
        //消息一旦就重新入队，就会拥有新的偏移量，不会存在上面所说的问题。

        /*
             kafka支持消息重新入队？
             kafka的ack机制不像其他mq,它不支持将消息重新放回队列
         */

    }

}
