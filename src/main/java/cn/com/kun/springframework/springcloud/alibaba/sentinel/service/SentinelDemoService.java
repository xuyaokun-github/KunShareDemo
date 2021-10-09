package cn.com.kun.springframework.springcloud.alibaba.sentinel.service;

import cn.com.kun.common.utils.ThreadUtils;
import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicLong;

import static cn.com.kun.springframework.springcloud.alibaba.sentinel.SentinelResourceConstants.*;

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
     * 测试限流和降级一起使用
     * 熔断策略： 慢比例调用
     *
     * @return
     */
    public String testLimitAndDegrade(){

        try (Entry entry = SphU.entry(RESOURCE_NAME_3)) {
            // 被保护的逻辑
            LOGGER.info("开始执行testLimitAndDegrade");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("结束执行testLimitAndDegrade");
        } catch (BlockException ex) {

            // 处理被流控的逻辑
            //com.alibaba.csp.sentinel.slots.block.flow.FlowException
            /*
                假如触发的是降级，拿到的将是 DegradeException
             */
            LOGGER.error("testLimitAndDegrade触发限流", ex);
        }
        // 被保护的逻辑
        LOGGER.info(ThreadUtils.getCurrentInvokeClassAndMethod());

        return ThreadUtils.getCurrentInvokeClassAndMethod() + "执行完毕";
    }

    /**
     * 测试限流和降级一起使用
     * 熔断策略： 异常数
     *
     * @return
     */
    public String testLimitAndDegrade2(){

        //下面的代码是反例
//        try (Entry entry = SphU.entry(RESOURCE_NAME_TESTLIMITANDDEGRADE2)) {
//            // 被保护的逻辑
//            LOGGER.info("开始执行testLimitAndDegrade2");
//            //模拟出现异常
//            int a = 1/0;
//            LOGGER.info("结束执行testLimitAndDegrade2");
//        } catch (Throwable ex) {
//            //因为业务方法可能抛出各种异常，假如捕获不到异常，无法进行统计，统计缺失就无法做进一步的熔断判断
//            //所以这里必须是用异常类的父类Throwable，也可以用Exception，Exception只不过范围小一点
//
//            //正确的统计方法
//            if (!BlockException.isBlockException(ex)) {
//                LOGGER.error("testLimitAndDegrade2出现非Block异常");
//                //统计后，后续就能进行熔断处理
//                Tracer.trace(ex);
//            }else {
//                LOGGER.error("testLimitAndDegrade2出现Block异常");
//            }
//
//            // 处理被流控的逻辑
//            //com.alibaba.csp.sentinel.slots.block.flow.FlowException
//            /*
//                假如触发的是降级，拿到的将是 DegradeException
//             */
//        }

        Entry entry = null;
        try {
            entry = SphU.entry(RESOURCE_NAME_TESTLIMITANDDEGRADE2);

            // Write your biz code here.
            // <<BIZ CODE>>
            // 被保护的逻辑
            LOGGER.info("开始执行testLimitAndDegrade2");
            //模拟出现异常
            int a = 1/0;
            LOGGER.info("结束执行testLimitAndDegrade2");
        } catch (Throwable t) {
            if (!BlockException.isBlockException(t)) {
                LOGGER.error("testLimitAndDegrade2出现非Block异常");
                Tracer.trace(t);
            }else {
                LOGGER.error("testLimitAndDegrade2出现Block异常");
            }
        } finally {
            if (entry != null) {
                entry.exit();
            }
        }

        // 被保护的逻辑
        LOGGER.info(ThreadUtils.getCurrentInvokeClassAndMethod());

        return ThreadUtils.getCurrentInvokeClassAndMethod() + "执行完毕";
    }

}
