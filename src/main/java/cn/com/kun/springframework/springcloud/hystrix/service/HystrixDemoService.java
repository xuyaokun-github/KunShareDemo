package cn.com.kun.springframework.springcloud.hystrix.service;

import cn.com.kun.common.utils.HttpClientUtils;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by xuyaokun On 2020/7/5 22:57
 * @desc:
 */
@Service
public class HystrixDemoService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "errorCallBack")
    public String method1() {
        //http://eureka-client-two/two/testHello2
        String result = restTemplate.getForObject("http://localhost:8082/two/testHello", String.class);
        System.out.println(result);
        return result;
    }

    @HystrixCommand(fallbackMethod = "errorCallBack")
    public String method2() {
        String result = HttpClientUtils.doGet("http://localhost:8082/two/testHello", null);
        System.out.println(result);
        return result;
    }

    //execution.timeout.enabled说的是执行超时，不影响execution.isolation.thread.timeoutInMilliseconds的设置
    @HystrixCommand(commandProperties = {
               @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),
              @HystrixProperty(name = "execution.timeout.enabled", value = "false")}, fallbackMethod = "errorCallBack")
    public String method4() {

        System.out.println("正在进行真实调用");
        String result = restTemplate.getForObject("http://localhost:8082/two/testHello", String.class);
        System.out.println(result);
        return result;
    }


    @HystrixCommand(fallbackMethod = "errorCallBack")
    public String method3() {
        String result = "000";
        int a = 1/0;
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
        return "sorry 触发服务降级";
    }


}
