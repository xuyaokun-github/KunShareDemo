package cn.com.kun.component.hystrixextend.aspect;

import cn.com.kun.component.hystrixextend.HystrixRateLimitExtend;
import cn.com.kun.component.hystrixextend.HystrixRateLimitValueHolder;
import cn.com.kun.component.spel.SpELHelper;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * 自定义Hystrix切面--实现按场景限流
 * 各个场景拥有各自的信号量
 * 为了保护下游，限制同时调用下游的请求数量，
 * 这种场景不需要考虑时间窗口，所以用Hystrix的限流算法是够用的
 *
 * TODO：
 * 通过反射修改HystrixCommand对象的值，会有多线程安全的问题
 *
 * author:xuyaokun_kzx
 * date:2021/7/2
 * desc:
*/
@Component
@Aspect
//@Order(-1)
//@Order(Ordered.LOWEST_PRECEDENCE)
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
//@Order(Ordered.HIGHEST_PRECEDENCE) //任何切面都不能定义成最高优先级
public class HystrixRateLimitExtendAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(HystrixRateLimitExtendAspect.class);

    @Autowired
    private SpELHelper spELHelper;

    @Autowired
    private HystrixRateLimitValueHolder hystrixRateLimitValueHolder;

    @Pointcut("@annotation(cn.com.kun.component.hystrixextend.HystrixRateLimitExtend)")
    public void pointCut() {

    }

    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) throws NoSuchFieldException, IllegalAccessException {

        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
//        Class<?> targetCls = joinPoint.getTarget().getClass();
//        Method method = targetCls.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());

        HystrixRateLimitExtend hystrixRateLimitExtend = method.getAnnotation(HystrixRateLimitExtend.class);

        //业务场景名
        String bizSceneName = hystrixRateLimitExtend.bizSceneName();
        //SpEL表达式
        String spELExp = hystrixRateLimitExtend.key();
        Object keyObj = spELHelper.generateKeyBySpEL(spELExp, joinPoint);
        String itemName = keyObj.toString();

        //替换限流值
        String rateLimitValue = hystrixRateLimitValueHolder.getRateLimitValue(bizSceneName, itemName);
        LOGGER.info("解析SpEL表达式得到的源对象：{} 对应限流值：{}", keyObj, rateLimitValue);

        if (StringUtils.isNotEmpty(rateLimitValue)){
            //假如存在指定的限流值，则进行替换
            // 获取方法上的HystrixCommand注解对象
            HystrixCommand hystrixCommand = method.getAnnotation(HystrixCommand.class);
            HystrixProperty[] hystrixProperties = hystrixCommand.commandProperties();

            //为了让不同场景拥有各自的信号量对象，必须设置不同的commandKey
            String newCommandKey = newCommandKey(bizSceneName, itemName);
            configureNewCommandKey(hystrixCommand, newCommandKey);

            //配置新的限流值
            configureNewHystrixProperties(hystrixProperties, rateLimitValue);

        }
    }

    @After(value = "pointCut()") //传入的是切入点的方法名
    public void after() {
//        LOGGER.info("After.");
    }


    private void configureNewCommandKey(HystrixCommand hystrixCommand, String newCommandKey) throws NoSuchFieldException, IllegalAccessException{
        //这个值不能拿，每次反射修改之后都会影响到oldCommandKey，因为hystrixCommand是一个单例
//            String oldCommandKey = hystrixCommand.commandKey();
        InvocationHandler h = Proxy.getInvocationHandler(hystrixCommand);
        Field hField = h.getClass().getDeclaredField("memberValues");
        hField.setAccessible(true);
        Map memberValues = (Map) hField.get(h);
        //替换值
        memberValues.put("commandKey", newCommandKey);
    }

    /**
     * 生成新的CommandKey
     * @param bizSceneName
     * @param itemName
     * @return
     */
    private String newCommandKey(String bizSceneName, String itemName) {
        /*
            bizSceneName加itemName肯定是唯一的，通过配置文件定义的，肯定唯一
         */
        return bizSceneName + "-" + itemName;
    }

    private void configureNewHystrixProperties(HystrixProperty[] hystrixProperties, String rateLimitValue) throws NoSuchFieldException, IllegalAccessException {

        for (HystrixProperty hystrixProperty : hystrixProperties) {
            //假如属性名等于超时时间，修改超时时间
            //对应源码中的executionIsolationSemaphoreMaxConcurrentRequests
            if ("execution.isolation.semaphore.maxConcurrentRequests".equals(hystrixProperty.name())) {
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
                //这里根据场景！动态修改HystrixCommand的值
                memberValues.put("value", rateLimitValue);
            }


        }

    }


}