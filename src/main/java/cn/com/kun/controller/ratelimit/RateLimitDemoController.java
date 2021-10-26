package cn.com.kun.controller.ratelimit;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.ratelimiter.RateLimit;
import cn.com.kun.controller.HelloController;
import cn.com.kun.service.RateLimitDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 向后限流
 *
 * author:xuyaokun_kzx
 * date:2021/8/20
 * desc:
*/
@RequestMapping("/rateLimit")
@RestController
public class RateLimitDemoController {

    private final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    RateLimitDemoService rateLimitDemoService;


    /**
     * 验证向前限流效果
     * @return
     */
    @RateLimit(mode = "forward")
    @GetMapping("/testRateLimitForward")
    public ResultVo testRateLimitForward(){


        return ResultVo.valueOfSuccess();
    }

    /**
     * 验证多线程并发不同的场景
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/testRateLimit")
    public ResultVo testRateLimit() throws InterruptedException {

        for (int i = 0; i < 15; i++) {
            new Thread(()->{
                ResultVo res = rateLimitDemoService.method(null, "DX");
            }).start();
        }
//        for (int i = 0; i < 5; i++) {
//            new Thread(()->{
//                ResultVo res = rateLimitDemoService.method(null, "DX");
//            }).start();
//        }
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                ResultVo res = rateLimitDemoService.method(null, "WX");
            }).start();
        }

        return ResultVo.valueOfSuccess();
    }

    /**
     * 验证向后限流和Hystrix熔断一起使用
     *
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/testRateLimit2")
    public ResultVo testRateLimit2() throws InterruptedException {

        for (int i = 0; i < 1; i++) {
            new Thread(()->{
                ResultVo res = rateLimitDemoService.method2(null, "DX");
            }).start();
        }
        return ResultVo.valueOfSuccess();
    }

    /**
     * 注意，向后限流和Hystrix熔断一起使用
     * 要注意Hystrix它本身也有限流，它的限流值默认值非常小,默认是10，
     * 注意这个坑
     *
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/testRateLimit3")
    public ResultVo testRateLimit3() throws InterruptedException {

        for (int i = 0; i < 15; i++) {
            new Thread(()->{
                ResultVo res = rateLimitDemoService.method2(null, "DX");
            }).start();
        }
        return ResultVo.valueOfSuccess();
    }



}
