package cn.com.kun.springframework.springcloud.hystrix.controller;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springcloud.hystrix.BizHandler;
import cn.com.kun.springframework.springcloud.hystrix.CustomHystrixCommand;
import cn.com.kun.springframework.springcloud.hystrix.CustomHystrixCommand2;
import cn.com.kun.springframework.springcloud.hystrix.service.HystrixDemoService;
import cn.com.kun.springframework.springcloud.hystrix.service.HystrixRateLimitDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/hystrix-demo")
@RestController
public class HystrixDemoController {

    @Autowired
    private HystrixDemoService hystrixDemoService;

    @Autowired
    private HystrixRateLimitDemoService hystrixRateLimitDemoService;

    @Autowired
    CustomHystrixCommand2 customHystrixCommand2;

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
     * 验证超时场景
     * http://localhost:8080/kunsharedemo/hystrix-demo/testRateLimit4
     * @return
     */
    @RequestMapping("/testRateLimit4")
    public ResultVo testRateLimit4(){

        /**
         * 用多个线程访问同一个方法
         * 起一个线程进行调用，验证超时熔断场景
         */
        for (int i = 0; i < 1; i++) {
            new Thread(()->{
                ResultVo res = hystrixRateLimitDemoService.method2(null, "WX");
            }).start();
        }
        return ResultVo.valueOfSuccess();
    }

    /**
     * 验证分场景限流
     * http://localhost:8080/kunsharedemo/hystrix-demo/testRateLimit5
     * @return
     */
    @RequestMapping("/testRateLimit5")
    public ResultVo testRateLimit5(){

        /**
         * 用多个线程访问同一个方法
         * 起一个线程进行调用，验证超时熔断场景
         */
        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                ResultVo res = hystrixRateLimitDemoService.method2(null, "WX");
            }).start();
        }
        return ResultVo.valueOfSuccess();
    }


    /**
     * 验证分场景限流--
     * 验证多个场景，同时调用，不同的限流值是否同时生效
     * 将超时时间设置成100秒，
     * A场景限流值为2，启动两个线程，执行10秒
     * B场景限流值设置为10，在A场景的线程启动之后再启动3个线程，观察是否会触发限流
     *
     * 实践发现，这种B的三个线程会被限流，为什么？
     * 是因为A的两个线程还没结束，信号量池的上限就仍是2
     * 一旦线程A结束之后，单独执行method3的B场景时，上限仍然是2（这是因为我还没有去修改commandKey）
     * 因为信号量对象有缓存，
     * 假如commandKey相同，用的信号量就是同一个，所以会造成限流错误
     *
     * http://localhost:8080/kunsharedemo/hystrix-demo/testRateLimit6
     * @return
     */
    @RequestMapping("/testRateLimit6")
    public ResultVo testRateLimit6() throws InterruptedException {

        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                ResultVo res = hystrixRateLimitDemoService.method3(null, "WX");
            }).start();
        }
        //A场景线程全部启动完毕之后，开始启动B场景线程
        Thread.sleep(1000);
        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                ResultVo res = hystrixRateLimitDemoService.method3(null, "DX");
            }).start();
        }
        return ResultVo.valueOfSuccess();
    }

    /**
     * 等testRateLimit6方法结束之后，再调用testRateLimit7
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/testRateLimit7")
    public ResultVo testRateLimit7() throws InterruptedException {

        for (int i = 0; i < 1; i++) {
            new Thread(()->{
                ResultVo res = hystrixRateLimitDemoService.method3(null, "DX");
            }).start();
        }
        return ResultVo.valueOfSuccess();

    }

    /**
     * 验证设置了不同的commandKey之后
     * B场景限流值为10，A场景限流值为2
     * 先启动10个B场景线程，接着启动2个A场景线程
     *  最后，再启动2个B线程，
     * 预期效果：前面两次启动的A和B 线程都没触发限流，会发现最后启动的这两个B线程会被限流
     * 测试通过，符合预期
     *
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/testRateLimit8")
    public ResultVo testRateLimit8() throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                ResultVo res = hystrixRateLimitDemoService.method3(null, "DX");
            }).start();
        }
        Thread.sleep(1000);
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                ResultVo res = hystrixRateLimitDemoService.method3(null, "WX");
            }).start();
        }
        Thread.sleep(1000);
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                ResultVo res = hystrixRateLimitDemoService.method3(null, "DX");
            }).start();
        }

        return ResultVo.valueOfSuccess();

    }

    /**
     * 并发场景下有问题！！！
     * @return
     * @throws InterruptedException
     */
    @RequestMapping("/testRateLimit9")
    public ResultVo testRateLimit9() throws InterruptedException {

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                ResultVo res = hystrixRateLimitDemoService.method3(null, "DX");
            }).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                ResultVo res = hystrixRateLimitDemoService.method3(null, "WX");
            }).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(()->{
                ResultVo res = hystrixRateLimitDemoService.method3(null, "DX");
            }).start();
        }
        return ResultVo.valueOfSuccess();
    }


    @GetMapping("/testRateLimit10")
    public ResultVo testRateLimit10(){

        //已知入参
        Map<String, String> paramMap = null;
        String sendChannel = "DX";
        BizHandler bizHandler = ()->{
            hystrixRateLimitDemoService.method4(paramMap, sendChannel);
        };

        for (int i = 0; i < 3; i++) {
            CustomHystrixCommand customHystrixCommand = new CustomHystrixCommand(bizHandler);
            new Thread(()->{
                customHystrixCommand.execute();
            }).start();
        }

        return ResultVo.valueOfSuccess();
    }

    @GetMapping("/testRateLimit11")
    public ResultVo testRateLimit11(){

        //已知入参
        Map<String, String> paramMap = null;
        String sendChannel = "DX";
        BizHandler bizHandler = ()->{
            hystrixRateLimitDemoService.method4(paramMap, sendChannel);
        };

        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                customHystrixCommand2.execute();
            }).start();
        }
        return ResultVo.valueOfSuccess();
    }

}
