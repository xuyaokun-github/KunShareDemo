package cn.com.kun.springframework.springcloud.feign.Interceptor;

import cn.com.kun.common.utils.ThreadUtils;
import org.apache.http.NoHttpResponseException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * 针对NoHttpResponseException的Feign重试切面
 *
 * author:xuyaokun_kzx
 * date:2023/9/14
 * desc:
*/
@ConditionalOnProperty(prefix = "feign.exception-aspect", value = {"enabled"}, havingValue = "true", matchIfMissing = false)
@Component
@Aspect
public class FeignExceptionRetryAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(FeignExceptionRetryAspect.class);

//    @Value("${feign.exception-aspect.package}")
//    private final String packageName = "";


//    @Pointcut("execution(* cn.com.kun.springframework.springcloud.feign.client.*.*(..))") //可以正常切入
//    @Pointcut("@annotation(org.springframework.cloud.openfeign.FeignClient)") //无法切入,因为FeignClient注解是加在类上，不是在方法上
//    @Pointcut("@target(org.springframework.cloud.openfeign.FeignClient)") //无法切入，启动报错
//    @Pointcut("execution(* " + packageName + ".*.*(..))") //无法切入（语法编译不过，包名无法注入到final变量）
    @Pointcut("@within(org.springframework.cloud.openfeign.FeignClient)") //可以正常切入(推荐用这个)
    public void pointCut(){


    }

    /*
     * 定义一个环绕通知
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        long startTime = System.currentTimeMillis();
        LOGGER.info("FeignExceptionRetryAspect around start", ThreadUtils.getCurrentInvokeClassAndMethod());

        Object obj = null;
        int retryCount = 3;
        while (true){
            try {
                obj = pjp.proceed();
            }catch (Exception e){
                //注意，NoHttpResponseException会被feign封装为feign.RetryableException再向上层抛出
                if (e instanceof NoHttpResponseException ||
                        (e.getCause() != null && e.getCause() instanceof NoHttpResponseException)){
                    if (retryCount > 0){
                        //直接return 即表示可以重试
                        retryCount--;
                        LOGGER.info("识别到NoHttpResponseException,准备进行重试，重试次数剩余次数：{}", retryCount);
                        continue;
                    }else {
                        throw new RuntimeException("Feign重试次数已到");
                    }
                }else {
                    throw e;
                }
            }
            break;
        }

        LOGGER.info("FeignExceptionRetryAspect around end, time cost:{}ms", System.currentTimeMillis() - startTime);
        return obj;
    }

}
