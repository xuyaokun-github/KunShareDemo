package cn.com.kun.service.impl;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.component.ratelimiter.RateLimit;
import cn.com.kun.service.RateLimitDemoService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RateLimitDemoServiceImpl implements RateLimitDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(RateLimitDemoServiceImpl.class);

    @RateLimit(bizSceneName = "sendmsg", key = "#sendChannel")
    @Override
    public ResultVo method(Map<String, String> paramMap, String sendChannel) {
        LOGGER.info("RateLimitDemoServiceImpl在调用第三方接口，场景：{}", sendChannel);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOGGER.info("RateLimitDemoServiceImpl结束调用第三方接口，场景：{}", sendChannel);
        return ResultVo.valueOfSuccess();
    }


    /**
     * 自定义的向后限流和Hystrix熔断一起配合使用
     * @param paramMap
     * @param sendChannel
     * @return
     */
    @RateLimit(bizSceneName = "sendmsg", key = "#sendChannel")
    @HystrixCommand(
            commandKey = "RateLimitDemoServiceImpl-method2",
            commandProperties = {
                    @HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE"), // 信号量隔离（用了ThreadLocal最好用信号量）
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1500"), //超时时间
                    @HystrixProperty(name = "fallback.isolation.semaphore.maxConcurrentRequests", value="200")// fallback单机最高并发
            },
            fallbackMethod = "timeOutFallbackMethod"//指定降级方法，在熔断和异常时会走降级方法
    )
    @Override
    public ResultVo method2(Map<String, String> paramMap, String sendChannel) {
        LOGGER.info("RateLimitDemoServiceImpl在调用第三方接口，场景：{}", sendChannel);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOGGER.info("RateLimitDemoServiceImpl结束调用第三方接口，场景：{}", sendChannel);
        return ResultVo.valueOfSuccess();
    }

    /**
     * 触发限流后执行的方法
     * @param paramMap
     * @param sendChannel
     */
    public ResultVo timeOutFallbackMethod(Map<String, String> paramMap, String sendChannel){
        LOGGER.info("触发超时降级,当前场景：{}", sendChannel);
        return ResultVo.valueOfError("触发超时降级");
    }


}
