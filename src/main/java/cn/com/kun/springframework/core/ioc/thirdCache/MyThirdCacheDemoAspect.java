package cn.com.kun.springframework.core.ioc.thirdCache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
//加入了这个注解，就相当于定义一个切面类，但是前提是该类也被定义为bean
@Aspect
public class MyThirdCacheDemoAspect {

    public final static Logger LOGGER = LoggerFactory.getLogger(MyThirdCacheDemoAspect.class);

    /**
     * 定义一个切入点 例子：cn.com.kun.controller这个包下的所有类的所有方法都会被切入
     */
    @Pointcut("execution(* cn.com.kun.springframework.core.ioc.thirdCache.*.*(..))")
    public void pointcut() {}

    /*
     * 定义一个环绕通知
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object obj = pjp.proceed();
        return obj;
    }

}
