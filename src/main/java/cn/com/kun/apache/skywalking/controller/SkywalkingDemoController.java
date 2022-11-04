package cn.com.kun.apache.skywalking.controller;

import cn.com.kun.apache.skywalking.service.SkywalkingDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/skywalking")
@RestController
public class SkywalkingDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SkywalkingDemoController.class);

    @Autowired
    SkywalkingDemoService skywalkingDemoService;

    @GetMapping("/test")
    public String testString() throws InterruptedException {

        LOGGER.info("method1");
        skywalkingDemoService.method1();
        return "kunghsu";
    }

}
