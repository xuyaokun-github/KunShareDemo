package cn.com.kun.component.memorycache.maintain.aspect;

import cn.com.kun.component.memorycache.maintain.annotation.EvictCacheNotice;
import cn.com.kun.component.memorycache.maintain.MemoryCacheNoticeProcessor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
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

import java.lang.reflect.Method;

/**
 * EvictCacheNotice注解对应的处理切面
 *
 * author:xuyaokun_kzx
 * date:2021/6/29
 * desc:
*/
@Component
@Aspect
public class EvictCacheNoticeAspect {

    private final static Logger LOGGER = LoggerFactory.getLogger(EvictCacheNoticeAspect.class);

    /**
     * 用于SpEL表达式解析.
     */
    private SpelExpressionParser parser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Autowired
    private MemoryCacheNoticeProcessor memoryCacheNoticeProcessor;

    @Pointcut("@annotation(cn.com.kun.component.memorycache.maintain.annotation.EvictCacheNotice)")
    public void pointCut(){

    }


    @After(value = "pointCut()")
    public void after(JoinPoint joinPoint) throws NoSuchMethodException {

        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
//        Class<?> targetCls = joinPoint.getTarget().getClass();
//        Method method = targetCls.getDeclaredMethod(methodSignature.getName(), methodSignature.getParameterTypes());
        // 获取方法上的EvictCacheNotice注解对象
        EvictCacheNotice evictCacheNotice = method.getAnnotation(EvictCacheNotice.class);
        Object keyObj = generateKeyBySpEL(evictCacheNotice.key(), joinPoint);
        LOGGER.info("解析SpEL表达式得到的源对象：{}", keyObj);
//        Object[] args = joinPoint.getArgs();
        //需要刷新的业务缓存管理器名字，一般每个业务层专用一个缓存管理器
        String configName = evictCacheNotice.configName();
        String key = keyObj.toString();//要刷新的key名
        //发送清缓存通知
        memoryCacheNoticeProcessor.notice(configName, key);
        LOGGER.info("发送清除内存缓存通知结束");
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
