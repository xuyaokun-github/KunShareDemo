package cn.com.kun.springframework.springcloud.alibaba.sentinel.extend.sentinelCompensate;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * author:xuyaokun_kzx
 * date:2021/7/7
 * desc:
*/
@Component
@Aspect
public class SentinelFlowControlAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(SentinelFlowControlAspect.class);

    @Pointcut("@annotation(cn.com.kun.springframework.springcloud.alibaba.sentinel.extend.sentinelCompensate.SentinelFlowControl)")
    public void pointCut(){

    }

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        // 获取方法上的SentinelFlowControl注解对象
        SentinelFlowControl sentinelFlowControl = method.getAnnotation(SentinelFlowControl.class);
        //限流资源名称
        String value = sentinelFlowControl.value();
        String bizName = sentinelFlowControl.bizName();
        //是否拼接集群名称
        long pauseTime = sentinelFlowControl.pauseTime();

        Object result = null;
        while (true){
            try (Entry entry = SphU.entry(value)) {
                //被保护逻辑
                result = pjp.proceed();
                break;
            } catch (Exception ex) {
                if (ex instanceof BlockException){
                    if(ex instanceof FlowException){
                        //com.alibaba.csp.sentinel.slots.block.flow.FlowException
                        LOGGER.warn("{}触发限流", bizName);
                        //假如出现了限流，由当前线程一直处理，或者是丢弃、或者是放到其他队列，由异步线程去逐个处理
                        try {
                            Thread.sleep(pauseTime);
                        } catch (InterruptedException e) {
                            LOGGER.error("{} InterruptedException", bizName);
                            Thread.currentThread().interrupt();
                        }
                    }else {
                        //熔断异常
                        LOGGER.warn("{}触发熔断异常", bizName);
                        throw ex;
                    }
                }else {
                    //非限流异常
                    LOGGER.warn("{}出现非限流异常", bizName);
                    throw ex;
                }
            }
        }

        return result;
    }


}
