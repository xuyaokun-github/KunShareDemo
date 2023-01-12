package cn.com.kun.kafka.controller;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.kafka.msg.MsgCacheTopicMsg;
import cn.com.kun.kafka.producer.MsgCacheProducerService;
import cn.com.kun.kafka.topicIsolation.producer.ProducerTopicProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@RequestMapping("/kafkaClient")
@RestController
public class KafkaClientDemoController {

    public final static Logger LOGGER = LoggerFactory.getLogger(KafkaClientDemoController.class);

    @Autowired
    private MsgCacheProducerService msgCacheProducerService;

    @Autowired
    private ProducerTopicProcessor producerTopicProcessor;

    @GetMapping("/testTopicIsolation")
    public ResultVo testTopicIsolation(){

        //使用 主题拆分组件
        String topicName = producerTopicProcessor.getTopic("SM01", "BATCH", "HIGH");
        String topicName2 = producerTopicProcessor.getTopic("PM01", "BATCH", "HIGH");
        MsgCacheTopicMsg msgCacheTopicMsg = new MsgCacheTopicMsg();
        msgCacheTopicMsg.setMsgId(UUID.randomUUID().toString());
        msgCacheTopicMsg.setCreateTIme(new Date());
        msgCacheProducerService.produce(msgCacheTopicMsg, topicName);
        msgCacheProducerService.produce(msgCacheTopicMsg, topicName2);
        return ResultVo.valueOfSuccess();
    }


}
