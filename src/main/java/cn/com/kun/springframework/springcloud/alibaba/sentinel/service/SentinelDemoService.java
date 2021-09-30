package cn.com.kun.springframework.springcloud.alibaba.sentinel.service;

import cn.com.kun.common.utils.ThreadUtils;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.concurrent.atomic.AtomicLong;

import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.RESOURCE_NAME;

@Service
public class SentinelDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SentinelDemoService.class);

    private AtomicLong sleepMillis = new AtomicLong(100L);

    @PostConstruct
    public void init(){

        for (int i = 0; i < 10; i++) {
            //起一个线程一直调用testSimpleLimit方法，让它一直有流量
            new Thread(()->{
                try {
                    while (true){
                        //
                        testSimpleLimit();
                        Thread.sleep(sleepMillis.get());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        }
    }


    /**
     * 简单限流
     * 该方法会被疯狂访问，暂时先不打日志
     */
    public String testSimpleLimit(){

        try (Entry entry = SphU.entry(RESOURCE_NAME)) {
            // 被保护的逻辑
//            LOGGER.info(ThreadUtils.getCurrentInvokeClassAndMethod());
        } catch (BlockException ex) {
            // 处理被流控的逻辑
            //com.alibaba.csp.sentinel.slots.block.flow.FlowException
//            LOGGER.error("触发限流", ex);
        }
        return ThreadUtils.getCurrentInvokeClassAndMethod() + "执行完毕";
    }

    /**
     * 修改睡眠时间，控制发送频率
     */
    public void changeSleepMillis(String sleepMillisStr){

        sleepMillis.set(Long.parseLong(sleepMillisStr));
    }


    /**
     * 简单限流(注解方式，直接能用，无法做额外的配置)
     */
    @SentinelResource(value = "testSimpleLimit2", blockHandler = "exceptionHandler")
    public String testSimpleLimit2(){

        // 被保护的逻辑
        LOGGER.info(ThreadUtils.getCurrentInvokeClassAndMethod());
        return ThreadUtils.getCurrentInvokeClassAndMethod() + "执行完毕";
    }


    // Block 异常处理函数，参数最后多一个 BlockException，其余与原函数一致.
    public String exceptionHandler(BlockException ex) {
        LOGGER.error("触发限流", ex);
        return "请求过于频繁";
    }

    /**
     * 多规则限流--分场景限流
     */

}
