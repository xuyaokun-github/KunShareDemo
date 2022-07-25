package cn.com.kun.springframework.springcloud.alibaba.sentinel.controller;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.service.SentinelDemoService;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.service.SentinelFlowControlDemoService;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.service.flowMonitor.SentinelExtendService;
import cn.com.kun.springframework.springcloud.alibaba.sentinel.service.scenelimit.SentinelSceneLimitDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RequestMapping("/sentinel-demo")
@RestController
public class SentinelDemoController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SentinelDemoController.class);


    @Autowired
    private SentinelDemoService sentinelDemoService;

    @Autowired
    private SentinelExtendService sentinelExtendService;

    @Autowired
    private SentinelSceneLimitDemoService sceneLimitDemoService;

    @Autowired
    private SentinelFlowControlDemoService sentinelFlowControlDemoService;


    @GetMapping("/testSimpleLimit")
    public String testSimpleLimit() throws FileNotFoundException {

        for (int i = 0; i < 1; i++) {
            new Thread(()->{
                for (int j = 0; j < 3; j++) {
                    sentinelDemoService.testSimpleLimit();
                }
            }).start();
        }
        return "OK";
    }

    @GetMapping("/testSimpleLimit2")
    public String testSimpleLimit2() throws FileNotFoundException {

        for (int i = 0; i < 1; i++) {
            new Thread(()->{
                for (int j = 0; j < 3; j++) {
                    sentinelDemoService.testSimpleLimit2();
                }
            }).start();
        }
        return "OK";
    }

    /**
     * 不要起异步线程
     * @return
     * @throws FileNotFoundException
     */
    @GetMapping("/testSimpleLimit3")
    public String testSimpleLimit3() throws FileNotFoundException {

        for (int i = 0; i < 4; i++) {
            sentinelDemoService.testSimpleLimit2();
        }
        return "OK";
    }

    /**
     * @return
     * @throws FileNotFoundException
     */
    @GetMapping("/testLimitAndDegrade")
    public String testLimitAndDegrade() throws FileNotFoundException {

        for (int i = 0; i < 10; i++) {
            sentinelDemoService.testLimitAndDegrade();
        }
        return "OK";
    }

    @GetMapping("/testLimitAndDegrade2")
    public String testLimitAndDegrade2() throws Exception {

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 5; j++) {
                    sentinelDemoService.testLimitAndDegrade2();
                }
            }).start();
        }
//        for (int i = 0; i < 20; i++) {
//            sentinelDemoService.testLimitAndDegrade2();
//        }
        return "OK";
    }



    @GetMapping("/testGetAllJsonInfo")
    public String testGetAllJsonInfo() throws Exception {

        return JacksonUtils.toJSONString(sentinelExtendService.getAllJsonInfo());
    }

    @GetMapping("/testChangeSleepMillis")
    public String testChangeSleepMillis(@RequestParam("sleep") String sleep) throws Exception {

        sentinelDemoService.changeSleepMillis(sleep);
        return "OK";
    }

    @GetMapping("/testSceneLimit")
    public String testSceneLimit() throws Exception {

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 9; j++) {
                    ResultVo res = sceneLimitDemoService.method(null, "DX");
                }
            }).start();
        }
//        for (int i = 0; i < 1; i++) {
//            new Thread(()->{
//                for (int j = 0; j < 9; j++) {
//                    ResultVo res = sceneLimitDemoService.method(null, "WX");
//                }
//            }).start();
//        }
        return "OK";
    }


    @GetMapping("/testSceneLimit2")
    public String testSceneLimit2() throws Exception {

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                for (int j = 0; j < 9; j++) {
                    ResultVo res = sceneLimitDemoService.method2(null, "DX");
                }
            }).start();
        }
//        for (int i = 0; i < 1; i++) {
//            new Thread(()->{
//                for (int j = 0; j < 9; j++) {
//                    ResultVo res = sceneLimitDemoService.method(null, "WX");
//                }
//            }).start();
//        }
        return "OK";
    }



    @GetMapping("/testSentinelFlowControl")
    public String testSentinelFlowControl() throws Exception {

        for (int i = 0; i < 10; i++) {
            new Thread(()->{

                for (int j = 0; j < 500; j++) {
                    String res = sentinelFlowControlDemoService.test();
                }
            }).start();
        }
        return "OK";
    }

    @GetMapping("/testSentinelFlowControl-make-exception")
    public String testSentinelFlowControlMakeException() throws Exception {

        sentinelFlowControlDemoService.makeException();
        return "OK";
    }
}
