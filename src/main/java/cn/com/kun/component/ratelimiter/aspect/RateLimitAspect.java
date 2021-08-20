package cn.com.kun.component.ratelimiter.aspect;

import cn.com.kun.component.ratelimiter.RateLimit;
import cn.com.kun.component.ratelimiter.RateLimiterHolder;
import cn.com.kun.component.ratelimiter.exception.RateLimitException;
import cn.com.kun.component.spel.SpELHelper;
import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_HANDLER_ATTRIBUTE;

/**
 * RateLimit注解对应的处理切面
 *
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
//设置限流切面为最高优先级，让它在HystrixCommandAspect切面之前起作用，避免HystrixCommandAspect切面的多余操作
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
@Component
@Aspect
public class RateLimitAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(RateLimitAspect.class);

    @Autowired
    private SpELHelper spELHelper;

    @Autowired
    private RateLimiterHolder rateLimiterHolder;

    @Pointcut("@annotation(cn.com.kun.component.ratelimiter.RateLimit)")
    public void pointCut(){

    }

    @Before(value = "pointCut()")
    public void before(JoinPoint joinPoint) throws NoSuchMethodException {

        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        if (rateLimit.mode().equals("forward")){
            //向前限流
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (servletRequestAttributes != null && servletRequestAttributes.getRequest() != null){
                //避免误用，进行非空判断
                HttpServletRequest request = servletRequestAttributes.getRequest();
                Object handlerMethod = request.getAttribute(BEST_MATCHING_HANDLER_ATTRIBUTE);
                //包含上下文path
//                String uri = request.getRequestURI();
                //从http://开始
//                String url = request.getRequestURL().toString();
                //不含上下文
                String servletPath = request.getServletPath();
                String controllerName = getControllerName(handlerMethod);
                RateLimiter rateLimiter = rateLimiterHolder.chooseForwardRateLimiter(controllerName, servletPath);
                if (rateLimiter != null ){
                    if (!rateLimiter.tryAcquire()){
                        String msg = String.format("[request url：%s]触发限流，请稍后重试", servletPath);
                        reject(msg);
                    }
                }
            }

        }else {
            //向后限流
            String bizSceneName = null;
            String itemName = null;
            if (StringUtils.isNotEmpty(rateLimit.key())){
                Object keyObj = spELHelper.generateKeyBySpEL(rateLimit.key(), joinPoint);
                LOGGER.info("解析SpEL表达式得到的源对象：{}", keyObj);
                bizSceneName = rateLimit.bizSceneName();
                itemName = keyObj.toString();
            }

            RateLimiter rateLimiter = rateLimiterHolder.chooseBackwardRateLimiter(bizSceneName, itemName);
            if (rateLimiter != null){
                if (!rateLimiter.tryAcquire()){
                    //触发限流
                    String msg = String.format("[业务场景:%s 子场景：%s]触发限流，请稍后重试", bizSceneName, itemName);
                    reject(msg);
                }else {
                    //未到达阈值，放行
                }

            }else {
                //获取不到限流器，不做处理
            }
        }

    }

    /**
     * 执行限流拒绝，向上层抛出限流异常
     * @param msg
     */
    private void reject(String msg){
        LOGGER.warn(msg);
        throw new RateLimitException(msg);
    }

    private String getControllerName(Object handlerMethod) {
        if (handlerMethod != null && handlerMethod instanceof  HandlerMethod){
            HandlerMethod method = (HandlerMethod) handlerMethod;
            //获取实际调用的方法
            Method classMethod = method.getMethod();
            //获取方法所在的类对应的class对象
            Class clazz = classMethod.getDeclaringClass();
            String clazzName = clazz.getSimpleName();
            clazzName = transFisrtLetterToLower(clazzName);
            return clazzName;
            //返回bean名并不可靠，因为可能控制层的bean会被代理，className也有可能发生变化
//            Object bean = method.getBean();
//            Class clazz = method.getBeanType();
//            String clazzName = clazz.getSimpleName();
//            if (bean instanceof String){
//                //有时候bean不一定是字符串类型
//                //返回bean名
//                return (String) bean;
//            }else {
//                return clazzName;
//            }
        }
        return "";
    }

    private String transFisrtLetterToLower(String clazzName) {
        String firstLetter = clazzName.substring(0, 1);
        return firstLetter.toLowerCase() + clazzName.substring(1);
    }


}
