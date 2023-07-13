package cn.com.kun.kafka.dynamicConsume.other;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.kafka.dynamicConsume.properties.KafkaDynamicConsumeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@ConditionalOnProperty(prefix = "kunsharedemo.kafkaclients", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@RequestMapping("/kafka-pauseConsume")
@RestController
public class KafkaPauseConsumeDemoController {

    public final static Logger LOGGER = LoggerFactory.getLogger(KafkaPauseConsumeDemoController.class);

    @Autowired
    private CustomTopicOneConsumerService2 customTopicOneConsumerService;

    @Autowired
    private CustomConsumeSwitchQuerierImpl consumeSwitchQuerier;

    @Autowired
    private KafkaDynamicConsumeProperties dynamicConsumeProperties;

    @PostConstruct
    public void init(){

        dynamicConsumeProperties.toString();
    }


    @GetMapping("/close")
    public ResultVo close(){

        customTopicOneConsumerService.setConsumeSwitch(false);
        consumeSwitchQuerier.setConsumeSwitch(false);

        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/open")
    public ResultVo open(){

        customTopicOneConsumerService.setConsumeSwitch(true);
        consumeSwitchQuerier.setConsumeSwitch(true);
        return ResultVo.valueOfSuccess();
    }
}
