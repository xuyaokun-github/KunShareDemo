package cn.com.kun.springframework.springredis.controller;

import cn.com.kun.springframework.springredis.datecountControl.DateCountControlDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * N天不能超过M次需求
 *
 * Created by xuyaokun On 2022/1/14 13:42
 * @desc:
 */
@RequestMapping("/spring-redis-dateCountControl")
@RestController
public class DateCountControlDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(DateCountControlDemoController.class);

    @Autowired
    DateCountControlDemoService dateCountControlDemoService;

    /***
     * @return
     */
    @GetMapping(value = "/test1")
    public String test1(){

        dateCountControlDemoService.test1();
        return "OK";
    }

    @GetMapping(value = "/test2")
    public String test2(){

        dateCountControlDemoService.test2();
        return "OK";
    }

    @GetMapping(value = "/test3")
    public String test3(){

        dateCountControlDemoService.test3();
        return "OK";
    }

    @GetMapping(value = "/testDateCountStatisticsHelper")
    public String testDateCountStatisticsHelper(){
        dateCountControlDemoService.testDateCountStatisticsHelper();
        return "OK";
    }


    @GetMapping(value = "/testDateCountStatisticsHelperDelete")
    public String testDateCountStatisticsHelperDelete(){
        dateCountControlDemoService.testDateCountStatisticsHelperDelete();
        return "OK";
    }

}
