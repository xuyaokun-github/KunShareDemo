package cn.com.kun.springframework.springkafka.producer;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.kafka.msg.MsgCacheTopicMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;


/**
 * 消息生产者
 *
 * @author xuyaokun
 * @date 2020/3/16 11:44
 */
@Component
public class MyKafkaProducer {

    private static Logger logger = LoggerFactory.getLogger(MyKafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //发送消息方法
    public void sendOne() {

        String message = buildMessage();
                //"kunghsu msg................" + DateUtils.now();
        logger.info("发送消息 ----->>>>>  message = {}", message);
        //主题是hello。这个主题，不需要存在，假如不存在，会进行创建。但是默认创建的分区只有一个
        kafkaTemplate.send("hello-topic", message);
        //send方法有很多重载，可以指定key和分区
    }

    public void sendOneByGet() throws ExecutionException, InterruptedException {

        String message = buildMessage();
        //"kunghsu msg................" + DateUtils.now();
        logger.info("发送消息 ----->>>>>  message = {}", message);
        //主题是hello。这个主题，不需要存在，假如不存在，会进行创建。但是默认创建的分区只有一个
        //send方法有很多重载，可以指定key和分区
        //get方法，可以将异步转同步
        SendResult sendResult = kafkaTemplate.send("hello-topic", message).get();
        logger.info("发送消息sendResult：{}", sendResult);


    }

    private String buildMessage() {
        MsgCacheTopicMsg msgCacheTopicMsg = new MsgCacheTopicMsg();
        msgCacheTopicMsg.setStatusCode("" + ThreadLocalRandom.current().nextInt(10));
        msgCacheTopicMsg.setCreateTIme(new Date());
        msgCacheTopicMsg.setMsgId("" + System.currentTimeMillis());
        return JacksonUtils.toJSONString(msgCacheTopicMsg);
    }

    public void sendBatch() {

        for(int i=0;i < 10;i++){
            String message = buildMessage();
            logger.info("发送消息 ----->>>>>  message = {}", message);
            //主题是hello。这个主题，不需要存在，假如不存在，会进行创建。但是默认创建的分区只有一个
            kafkaTemplate.send("hello-topic", message);
            //send方法有很多重载，可以指定key和分区
        }

    }


}