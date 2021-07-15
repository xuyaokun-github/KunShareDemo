package cn.com.kun.kafka.producer;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;


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
    public void send() {

        for(int i=0;i<5;i++){
            String message = "kunghsu msg................" + System.currentTimeMillis();
            logger.info("发送消息 ----->>>>>  message = {}", JSONObject.toJSONString(message));
            //主题是hello。这个主题，不需要存在，假如不存在，会进行创建。但是默认创建的分区只有一个
            kafkaTemplate.send("hello-topic", JSONObject.toJSONString(message));
            //send方法有很多重载，可以指定key和分区
        }


    }



}