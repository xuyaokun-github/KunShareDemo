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

    @RequestMapping("/send")
    public String send(HttpServletRequest request){

        producer.send();
        return "kunghsu";
    }

}
