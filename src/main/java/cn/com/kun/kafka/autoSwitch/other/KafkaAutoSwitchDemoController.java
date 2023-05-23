package cn.com.kun.kafka.autoSwitch.other;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.kafka.controller.KafkaClientDemoController;
import cn.com.kun.kafka.msg.MsgCacheTopicMsg;
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
@RequestMapping("/kafka-auto-switch")
@RestController
public class KafkaAutoSwitchDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaClientDemoController.class);

    @Autowired
    private KafkaAutoSwitchProducerDemoService kafkaAutoSwitchProducerService;

    @GetMapping("/testAutoSwitch")
    public ResultVo testAutoSwitch(){

        //使用 主题拆分组件
        String topicName = "autoswitch-topic";
        MsgCacheTopicMsg msgCacheTopicMsg = new MsgCacheTopicMsg();
        msgCacheTopicMsg.setMsgId(UUID.randomUUID().toString());
        msgCacheTopicMsg.setCreateTIme(new Date());
        kafkaAutoSwitchProducerService.produceForAutoSwitch(msgCacheTopicMsg, topicName);
        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/testAutoSwitch2")
    public ResultVo testAutoSwitch2(){

        //使用 主题拆分组件
        String topicName = "autoswitch-topic2";
        MsgCacheTopicMsg msgCacheTopicMsg = new MsgCacheTopicMsg();
        msgCacheTopicMsg.setMsgId(UUID.randomUUID().toString());
        msgCacheTopicMsg.setCreateTIme(new Date());
        kafkaAutoSwitchProducerService.produceForAutoSwitchForTopic2(msgCacheTopicMsg, topicName);
        return ResultVo.valueOfSuccess();
    }
}
