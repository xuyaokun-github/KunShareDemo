package cn.com.kun.component.hystrixextend.aspect;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 自定义Hystrix切面
 * 可以在这个界面里修改HystrixCommand注解的值
 *
 * author:xuyaokun_kzx
 * date:2021/7/2
 * desc:
*/
//@Component
//@Aspect
//@Order(Integer.MIN_VALUE)
public class CustomHystrixCommandAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomHystrixCommandAspect.class);

    @Pointcut("@annotation(com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand)")
    public void pointCut() {

    }

    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {

        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
//        Class<?> targetCls = joinPoint.getTarget().getClass();
//        Method method = targetCls.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());




        // 获取方法上的HystrixCommand注解对象
        HystrixCommand hystrixCommand = method.getAnnotation(HystrixCommand.class);
        HystrixProperty[] hystrixProperties = hystrixCommand.commandProperties();
        for (HystrixProperty hystrixProperty : hystrixProperties) {
            //假如属性名等于超时时间，修改超时时间
            if ("execution.isolation.thread.timeoutInMilliseconds".equals(hystrixProperty.name())) {
                /*
                    这里获取到的hystrixProperty是一个代理对象
                    根据代理对象获取到代理处理程序，代理处理程序里有个属性叫memberValues
                    通过反射拿到memberValues
                 */
                InvocationHandler h = Proxy.getInvocationHandler(hystrixProperty);
                Field hField = h.getClass().getDeclaredField("memberValues");
                hField.setAccessible(true);
                Map memberValues = (Map) hField.get(h);
                //替换值
                memberValues.put("value", "6000");
            }

            //这里根据场景！动态修改HystrixCommand的值
        }

    }


}