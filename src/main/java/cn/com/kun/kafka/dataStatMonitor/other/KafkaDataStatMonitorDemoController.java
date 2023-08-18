package cn.com.kun.kafka.dataStatMonitor.other;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.kafka.controller.KafkaClientDemoController;
import cn.com.kun.kafka.dataStatMonitor.lag.TopicLagMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static cn.com.kun.kafka.dataStatMonitor.constants.DataStatConstant.MSG_TYPE_HEADER_KEY_NAME;

@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@RequestMapping("/kafka-dataStatMonitor")
@RestController
public class KafkaDataStatMonitorDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaDataStatMonitorDemoController.class);

    @Autowired
    private TopicLagMonitor topicLagMonitor;

    @Autowired
    private KafkaDataStatMonitorDemoProducerService kafkaDataStatMonitorDemoProducerService;

    @GetMapping("/testDataStatMonitor")
    public ResultVo testDataStatMonitor(){

        long lag = topicLagMonitor.getTotalLagInfo("autoswitch-topic");
        LOGGER.info("监控到的堆积量:{}", lag);
        return ResultVo.valueOfSuccess(lag);
    }

    @GetMapping("/testDataStatMonitorAll")
    public ResultVo testDataStatMonitorAll(){

        Map<String, Long> allTopicsLagInfo = topicLagMonitor.getAllTopicsLagInfo();
        LOGGER.info("监控到的堆积量:{}", allTopicsLagInfo);
        return ResultVo.valueOfSuccess(allTopicsLagInfo);
    }

    @GetMapping("/testProduce")
    public ResultVo testProduce(){

        Map<String, String> msgVO = new HashMap<>();
        msgVO.put("uuid", UUID.randomUUID().toString());
        msgVO.put(MSG_TYPE_HEADER_KEY_NAME, ThreadLocalRandom.current().nextInt(10)%2==0?"AAA":"BBB");

        kafkaDataStatMonitorDemoProducerService.produce(msgVO, "dataStatMonitor-topic");
        return ResultVo.valueOfSuccess();
    }


}
