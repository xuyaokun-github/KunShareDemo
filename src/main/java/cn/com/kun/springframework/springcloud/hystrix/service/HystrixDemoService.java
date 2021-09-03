package cn.com.kun.springframework.springcloud.hystrix.service;

import cn.com.kun.common.exception.BizException;
import cn.com.kun.common.exception.MyHystrixDemoException;
import cn.com.kun.common.utils.HttpClientUtils;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by xuyaokun On 2020/7/5 22:57
 * @desc:
 */
@Service
public class HystrixDemoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(HystrixDemoService.class);

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(
            fallbackMethod = "errorCallBack",
            commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),
            @HystrixProperty(name = "execution.timeout.enabled", value = "true")})
    public String method1() {
        LOGGER.info("当前执行业务方法的线程名：{}", Thread.currentThread().getName());
        String result = "ok";
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < (120 * 1000)){

        }
//        try {
//            Thread.sleep(60000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        LOGGER.info("(结束)当前执行业务方法的线程名：{}", Thread.currentThread().getName());
        return result;
    }

    /**
     * 验证 限流异常
     * @return
     */
    @HystrixCommand(
            fallbackMethod = "errorCallBack",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000000"),
                    @HystrixProperty(name = "execution.timeout.enabled", value = "true")},
            threadPoolKey = "HystrixDemoService-method11", //假如希望本方法用独立的线程池，必须指定threadPoolKey
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "1"),
//                    @HystrixProperty(name = "maximumSize", value = "100"), //这个值在这里定义会报错，是个bug
                    @HystrixProperty(name = "maxQueueSize", value = "2"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "10"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "1"),
            })
    public String method11() {
        LOGGER.info("当前执行业务方法的线程名：{}", Thread.currentThread().getName());
        String result = "ok";
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < (10 * 1000)){

        }
        LOGGER.info("(结束)当前执行业务方法的线程名：{}", Thread.currentThread().getName());
        return result;
    }


    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")},
            fallbackMethod = "errorCallBack")
    public String method2() {
        String result = HttpClientUtils.doGet("http://localhost:8082/two/testHello", null);
        System.out.println(result);
        return result;
    }

    //execution.timeout.enabled说的是执行超时，不影响execution.isolation.thread.timeoutInMilliseconds的设置
    @HystrixCommand(commandProperties = {
               @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
              @HystrixProperty(name = "execution.timeout.enabled", value = "false")},
            fallbackMethod = "errorCallBack")
    public String method4() {

        System.out.println("正在进行真实调用");
        String result = restTemplate.getForObject("http://localhost:8082/two/testHello", String.class);
        System.out.println(result);
        return result;
    }


    /**
     * 模拟出异常时的降级
     * @return
     */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "50000000")
    },
//            ignoreExceptions = {BizException.class},
                    fallbackMethod = "errorCallBack")
    public String method3() {
        String result = "000";

//        int a = 1/0;

        //抛出自定义异常，也可以触发降级
        if (true){
            throw new BizException("业务异常");
        }

        return result;
    }

    /**
     * 例子--设置只有超时和限流发生时才触发熔断
     *
     * @param flag
     * @return
     */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
    },
            ignoreExceptions = {BizException.class},
            fallbackMethod = "errorCallBack2")
    public String method31(String flag) {

        String result = "000";
        try {
            //抛出自定义异常，也可以触发降级
            if ("true".equals(flag)){
                throw new BizException("业务异常");
            }
        }catch (Exception e){
            if (e instanceof BizException) {
                throw e;
            }else {
                throw new BizException(e.getMessage());
            }
        }

        //模拟一个执行超时
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * ExceptionNotWrappedByHystrix的使用
     * @param flag
     * @return
     */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
    },
            fallbackMethod = "errorCallBack2")
    public String method32(String flag) {

        String result = "000";
        if ("true".equals(flag)){
            //程序可以自由决定，
            //假如不希望触发降级，就抛出MyHystrixDemoException（hystrix不会针对该异常做降级处理）
            //假如想走降级，那就抛出非MyHystrixDemoException异常
            throw new MyHystrixDemoException();
        }
        //模拟一个执行超时
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 演示熔断
     * @return
     */
    @HystrixCommand(fallbackMethod = "errorCallBack",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000"),//指定多久超时，单位毫秒。超时进fallback
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),//判断熔断的最少请求数，默认是10；
                    // 只有在一个统计窗口内处理的请求数量达到这个阈值，才会进行熔断与否的判断
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),//判断熔断的阈值，默认值50，
                    // 表示在一个统计窗口内有50%的请求处理失败，会触发熔断
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds" , value = "60000") //熔断多少毫秒后开始尝试请求 默认5000ms
            }
    )
    public String method5() {
        System.out.println("正在进行真实调用");
        String result = restTemplate.getForObject("http://localhost:8082/two/testHello", String.class);
        System.out.println(result);
        return result;
    }

    /*
    ,
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "8"),
                    @HystrixProperty(name = "maxQueueSize", value = "100"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1440")
            }
     */

    /**
     * 降级方法
     * （注意这里的方法签名必须和@HystrixCommand注解修饰的方法签名一致）
     * @return
     */
    public String errorCallBack() {
        LOGGER.info("触发降级：{}", Thread.currentThread().getName());
        return "sorry 触发服务降级";
    }

    public String errorCallBack2(String flag) {

        return "sorry 触发超时降级";
    }
}
