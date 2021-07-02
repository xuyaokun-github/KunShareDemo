package cn.com.kun.springframework.springcloud.hystrix.controller;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springcloud.hystrix.service.HystrixDemoService;
import cn.com.kun.springframework.springcloud.hystrix.service.HystrixRateLimitDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/hystrix-demo")
@RestController
public class HystrixDemoController {

    @Autowired
    private HystrixDemoService hystrixDemoService;

    @Autowired
    private HystrixRateLimitDemoService hystrixRateLimitDemoService;

    @RequestMapping("/test1")
    public String testHello(){

        String res = hystrixDemoService.method1();
        System.out.println(res);
        return res;
    }

    @RequestMapping("/test2")
    public String test2(){

        String res = hystrixDemoService.method2();
        System.out.println(res);
        return res;
    }

    @RequestMapping("/test3")
    public String test3(){

        String res = hystrixDemoService.method3();
        System.out.println(res);
        return res;
    }

    @RequestMapping("/test4")
    public String test4(){

        String res = hystrixDemoService.method4();
        System.out.println(res);
        return res;
    }

    @RequestMapping("/test5")
    public String test5(){

        String res = hystrixDemoService.method5();
        System.out.println(res);
        return res;
    }

    /**
     *
     * @return
     */
    @RequestMapping("/testRateLimit")
    public ResultVo testRateLimit(){

        ResultVo res = hystrixRateLimitDemoService.method(null, null);
        return res;
    }

    /**
     *
     * @return
     */
    @RequestMapping("/testRateLimit2")
    public ResultVo testRateLimit2(){

        for (int i = 0; i < 600; i++) {
            ResultVo res = hystrixRateLimitDemoService.method(null, null);
        }
        return ResultVo.valueOfSuccess();
    }

    /**
     *
     * @return
     */
    @RequestMapping("/testRateLimit3")
    public ResultVo testRateLimit3(){

        /**
         * 用多个线程访问同一个方法
         */
        for (int i = 0; i < 1; i++) {
            new Thread(()->{
                ResultVo res = hystrixRateLimitDemoService.method(null, null);
            }).start();
        }
        return ResultVo.valueOfSuccess();
    }

    /**
     *
     * @return
     */
    @RequestMapping("/testRateLimit4")
    public ResultVo testRateLimit4(){

        /**
         * 用多个线程访问同一个方法
         */
        for (int i = 0; i < 1; i++) {
            new Thread(()->{
                ResultVo res = hystrixRateLimitDemoService.method2(null, "WX");
            }).start();
        }
        return ResultVo.valueOfSuccess();
    }

}
