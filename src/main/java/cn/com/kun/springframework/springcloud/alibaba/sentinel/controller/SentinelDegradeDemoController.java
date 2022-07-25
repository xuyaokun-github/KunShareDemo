package cn.com.kun.springframework.springcloud.alibaba.sentinel.controller;

import cn.com.kun.springframework.springcloud.alibaba.sentinel.service.SentinelDegradeDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RequestMapping("/sentinel-degrade-demo")
@RestController
public class SentinelDegradeDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SentinelDegradeDemoController.class);

    @Autowired
    private SentinelDegradeDemoService sentinelDegradeDemoService;

    @GetMapping("/testDegrade")
    public String testDegrade() throws FileNotFoundException {

        for (int j = 0; j < 10; j++) {
            sentinelDegradeDemoService.testDegrade();
        }
        return "OK";
    }


    @GetMapping("/testDegradeOneTimes")
    public String testDegradeOneTimes() throws FileNotFoundException {

//        for (int j = 0; j < 10; j++) {
            sentinelDegradeDemoService.testDegrade();
//        }
        return "OK";
    }


}
