package cn.com.kun.component.aspect;

import cn.com.kun.common.exception.RateLimitException;
import cn.com.kun.component.ratelimiter.RateLimit;
import cn.com.kun.component.ratelimiter.RateLimiterHolder;
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
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

/**
 * RateLimit注解对应的处理切面
 *
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
@Component
@Aspect
public class RateLimitAspect {

    public final static Logger LOGGER = LoggerFactory.getLogger(RateLimitAspect.class);

    /**
     * 用于SpEL表达式解析.
     */
    private SpelExpressionParser parser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

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
            String uri = servletRequestAttributes.getRequest().getRequestURI();
            String controllerName = rateLimit.controllerName();
//            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
//            GetMapping getMapping = method.getAnnotation(GetMapping.class);
            RateLimiter rateLimiter = rateLimiterHolder.chooseForwardRateLimiter(controllerName, uri);
            if (rateLimiter != null && !rateLimiter.tryAcquire()){
                LOGGER.info("触发限流，uri：{}", uri);
                throw new RateLimitException("777777", "触发限流，请稍后重试");
            }
        }else {
            //向后限流
            String bizSceneName = null;
            String itemName = null;
            if (StringUtils.isNotEmpty(rateLimit.key())){
                Object keyObj = generateKeyBySpEL(rateLimit.key(), joinPoint);
                LOGGER.info("解析SpEL表达式得到的源对象：{}", keyObj);
                bizSceneName = rateLimit.bizSceneName();
                itemName = keyObj.toString();
            }

            RateLimiter rateLimiter = rateLimiterHolder.chooseBackwardRateLimiter(bizSceneName, itemName);
            if (rateLimiter != null && !rateLimiter.tryAcquire()){
                //触发限流
                LOGGER.info("触发限流，方法名：{}", methodSignature.getName());
                throw new RateLimitException("777777", "触发限流，请稍后重试");
            }else {

            }
        }

    }

    /**
     * 根据SpEL表达式得到具体的值
     * ProceedingJoinPoint和 JoinPoint 都支持
     *
     *  根据SpEL表达式从上下文中计算出实际参数值
     *   如:
     *    @annotation(key="#student.name")
     *    public void method(Student student)
     *   可以解析出方法形参的某属性值，
     *   return "kunghsu";
     *
     * @param spELString
     * @param joinPoint
     * @return
     */
    public Object generateKeyBySpEL(String spELString, JoinPoint joinPoint) {
        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 使用spring的DefaultParameterNameDiscoverer获取方法形参名数组
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        // 解析过后的Spring表达式对象
        Expression expression = parser.parseExpression(spELString);
        // spring的表达式上下文对象
        EvaluationContext context = new StandardEvaluationContext();
        // 通过joinPoint获取被注解方法的形参
        Object[] args = joinPoint.getArgs();
        // 给上下文赋值
        for(int i = 0 ; i < args.length ; i++) {
            context.setVariable(paramNames[i], args[i]);
        }

        return expression.getValue(context);
    }

}
