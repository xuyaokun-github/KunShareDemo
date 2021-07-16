package cn.com.kun.kafka.controller;

import cn.com.kun.kafka.producer.MyKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/kafka")
@RestController
public class KafkaDemoController {

    @Autowired
    MyKafkaProducer producer;

    @RequestMapping("/sendOne")
    public String sendOne(HttpServletRequest request){

        producer.sendOne();
        return "kunghsu";
    }

    @RequestMapping("/sendBatch")
    public String sendBatch(HttpServletRequest request){

        producer.sendBatch();
        return "kunghsu";
    }

}
