package cn.com.kun.springframework.springcloud.hystrix;

import cn.com.kun.common.vo.ResultVo;
import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 注意一个实例只能被执行一次
 * Caused by: java.lang.IllegalStateException: This instance can only be executed once. Please instantiate a new instance.
 * 假如需要调多次execute方法，需要重新new CustomHystrixCommand对象
 * 但是重新new了这个对象，相当于限流就发挥不了作用
 * 因为对象是新的，计数就是从0开始！
 *
 * author:xuyaokun_kzx
 * date:2021/8/23
 * desc:
*/
//@Component
public class CustomHystrixCommand extends HystrixCommand {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomHystrixCommand.class);

    private String sendChannel;
    private Map<String, String> paramMap;
    //具体的业务逻辑层
    BizHandler bizHandler;

    public CustomHystrixCommand(BizHandler bizHandler) {

        //super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        super(buildSetter());
        this.bizHandler = bizHandler;
        //假如希望CustomHystrixCommand是动态的，就在创建CustomHystrixCommand对象时，动态设置不同的值
    }

    private static Setter buildSetter() {
        //com.netflix.hystrix.HystrixCommand.Setter
        HystrixCommand.Setter setter = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CmmandGroupKey"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("CommandKey"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("ThreadPoolKey"));

        HystrixCommandProperties.Setter propertiesSetter = HystrixCommandProperties.defaultSetter();
        propertiesSetter.withExecutionIsolationSemaphoreMaxConcurrentRequests(1);
        propertiesSetter.withFallbackIsolationSemaphoreMaxConcurrentRequests(200);
        setter.andCommandPropertiesDefaults(propertiesSetter);
        return setter;
    }

    @Override
    protected ResultVo run() throws Exception {

        //执行具体的业务逻辑
        LOGGER.info("执行具体业务逻辑");
        bizHandler.process();

        return ResultVo.valueOfSuccess("执行成功");
    }


}
