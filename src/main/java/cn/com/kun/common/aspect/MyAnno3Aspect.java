package cn.com.kun.common.aspect;

import cn.com.kun.common.utils.ThreadUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


@Component
@Aspect
public class MyAnno3Aspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyAnno3Aspect.class);

    @Pointcut("@annotation(cn.com.kun.common.annotation.MyAnno1)")
    public void pointCut(){

    }

    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) throws NoSuchMethodException {

        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        LOGGER.info("MyAnno1Aspect：{}", ThreadUtils.getCurrentInvokeClassAndMethod());

    }



}
