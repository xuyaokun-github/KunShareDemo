package cn.com.kun.component.aspect;

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
//数字越小，优先级越高
//@Order(Ordered.HIGHEST_PRECEDENCE + 1) //优先级注解真的能控制顺序
//@Order(1)
//@Order(-1)
//@Order(Ordered.LOWEST_PRECEDENCE)
//@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class MyAnno2Aspect {

    public final static Logger LOGGER = LoggerFactory.getLogger(MyAnno2Aspect.class);

    @Pointcut("@annotation(cn.com.kun.common.annotation.MyAnno2)")
    public void pointCut(){

    }

    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) throws NoSuchMethodException {

        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        LOGGER.info("MyAnno2Aspect：{}", ThreadUtils.getCurrentInvokeClassAndMethod());

    }



}
