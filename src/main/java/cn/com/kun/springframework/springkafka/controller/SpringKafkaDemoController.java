package cn.com.kun.springframework.springkafka.controller;

import cn.com.kun.springframework.springkafka.producer.MyKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@RequestMapping("/spring-kafka")
@RestController
public class SpringKafkaDemoController {

    private static Logger LOGGER = LoggerFactory.getLogger(SpringKafkaDemoController.class);

    @Autowired
    MyKafkaProducer producer;

    @GetMapping("/sendOne")
    public String sendOne(HttpServletRequest request){

        producer.sendOne();
        return "kunghsu";
    }

    @GetMapping("/sendOneByGet")
    public String sendOneByGet(HttpServletRequest request) throws ExecutionException, InterruptedException {

        producer.sendOneByGet();
        return "kunghsu";
    }

    @GetMapping("/sendBatch")
    public String sendBatch(HttpServletRequest request){

        producer.sendBatch();
        return "kunghsu";
    }

    @GetMapping("/sendLargeMsgAndTestException")
    public String sendLargeMsgAndTestException(HttpServletRequest request){

        producer.sendLargeMsgAndTestException();
        return "kunghsu";
    }

    @GetMapping("/sendSmallMsgAndTestException")
    public String sendSmallMsgAndTestException(HttpServletRequest request){

        producer.sendSmallMsgAndTestException();
        return "kunghsu";
    }
}
