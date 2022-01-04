package cn.com.kun.springframework.core.aop.aspect;

import cn.com.kun.common.utils.ThreadUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Component
@Aspect
public class MyAnno3Aspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyAnno3Aspect.class);

    @Pointcut("@annotation(cn.com.kun.springframework.core.aop.annotation.MyAnno1)")
    public void pointCut(){

    }

    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) throws NoSuchMethodException {

        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        LOGGER.info("MyAnno1Aspect before：{}", ThreadUtils.getCurrentInvokeClassAndMethod());

    }

    //注意点：假如使用这些通知注解，括号中假如不指定切入点，那么加载配置时就会报错
    /*
     * 定义一个返回通知
     */
    @AfterReturning("pointCut()")
    public void afterReturningMethod(){
        LOGGER.info("MyAnno1Aspect afterReturningMethod：{}", ThreadUtils.getCurrentInvokeClassAndMethod());
    }

    /*
     * 定义一个异常通知
     */
    @AfterThrowing(pointcut="pointCut()", throwing="e")
    public void afterThrowing(RuntimeException e) {
        LOGGER.info("MyAnno1Aspect afterThrowing：{}", ThreadUtils.getCurrentInvokeClassAndMethod());
    }
    /*
     * 定义一个后置通知
     */
    @After("pointCut()") //传入的是切入点的方法名
    public void after() {
        LOGGER.info("MyAnno1Aspect after：{}", ThreadUtils.getCurrentInvokeClassAndMethod());
    }

    /*
     * 定义一个环绕通知
     */
    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        LOGGER.info("MyAnno1Aspect around：{}", ThreadUtils.getCurrentInvokeClassAndMethod());
        Object obj = pjp.proceed();
        LOGGER.info("MyAnno1Aspect around：{}", ThreadUtils.getCurrentInvokeClassAndMethod());
        return obj;
    }

}
