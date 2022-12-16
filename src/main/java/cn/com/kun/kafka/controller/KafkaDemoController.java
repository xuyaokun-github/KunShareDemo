package cn.com.kun.kafka.controller;

import cn.com.kun.kafka.producer.MyKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;

@RequestMapping("/kafka")
@RestController
public class KafkaDemoController {

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

}
