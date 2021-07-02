package cn.com.kun.springframework.springcloud.hystrix.service;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.springcloud.hystrix.hystrixExtend.HystrixRateLimitExtend;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用hystrix做限流的demo
 *
 * author:xuyaokun_kzx
 * date:2021/7/2
 * desc:
*/
@Service
public class HystrixRateLimitDemoService {

    public final static Logger LOGGER = LoggerFactory.getLogger(HystrixRateLimitDemoService.class);

//    @HystrixCommand
    @HystrixCommand(
//            commandKey = "helloCommand",//缺省为方法名
//            threadPoolKey = "helloPool",//缺省为类名
////            commandProperties = {
////                    //超时时间
////                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
////            },
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE"), // 信号量隔离，因为业务方法用了ThreadLocal
//                    @HystrixProperty(name="execution.timeout.enabled", value = "false"), //
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500"), //超时时间
//                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value="50"),//触发熔断最小请求数量
//                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value="30"),//触发熔断的错误占比阈值
//                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value="3000"),//熔断器回复时间
                    //限流配置
                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value="30"),// 单机最高并发
                    @HystrixProperty(name = "fallback.isolation.semaphore.maxConcurrentRequests", value="50")// fallback单机最高并发
            },
//            threadPoolProperties = {
//                    //并发，缺省为10
//                    @HystrixProperty(name = "coreSize", value = "5"),
//                    @HystrixProperty(name = "maxQueueSize", value = "1")
//            },
            fallbackMethod = "fallbackMethod"//指定降级方法，在熔断和异常时会走降级方法
    )
    public ResultVo method(Map<String, String> paramMap, String sendChannel){

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("HystrixRateLimitDemoService在调用第三方接口");

        return ResultVo.valueOfSuccess();
    }


    /**
     * 触发限流后执行的方法
     * @param paramMap
     * @param sendChannel
     */
    public ResultVo fallbackMethod(Map<String, String> paramMap, String sendChannel){
        LOGGER.info("触发限流后执行的方法");
        return ResultVo.valueOfError("触发限流");
    }


    @HystrixRateLimitExtend(bizSceneName="sendmsg", key = "#sendChannel")
    @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE"), // 信号量隔离，因为业务方法用了ThreadLocal
                    //限流配置
                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value="30"),// 单机最高并发
                    @HystrixProperty(name = "fallback.isolation.semaphore.maxConcurrentRequests", value="200")// fallback单机最高并发
            },
            fallbackMethod = "fallbackMethod"//指定降级方法，在熔断和异常时会走降级方法
    )
    public ResultVo method2(Map<String, String> paramMap, String sendChannel){

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        /**
         * 怎么根据发送渠道，结合@HystrixCommand配置不同的限流值？
         * 不同的渠道，设置不同的限流值，怎么做？
         */
        LOGGER.info("HystrixRateLimitDemoService在调用第三方接口");

        return ResultVo.valueOfSuccess();
    }

}
