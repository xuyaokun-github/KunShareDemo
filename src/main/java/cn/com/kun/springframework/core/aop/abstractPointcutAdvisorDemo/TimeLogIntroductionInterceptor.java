package cn.com.kun.springframework.core.aop.abstractPointcutAdvisorDemo;

import cn.com.kun.common.utils.DateUtils;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 通知实现类
 * author:xuyaokun_kzx
 * date:2021/10/26
 * desc:
*/
public class TimeLogIntroductionInterceptor implements IntroductionInterceptor, BeanFactoryAware {

    private final static Logger LOGGER = LoggerFactory.getLogger(TimeLogIntroductionInterceptor.class);

    /**
     * 这里就是处理实际被代理的逻辑，在前后织入代理逻辑
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        /*
            MethodInvocation是什么呢？
            需要看aop源码分析，是aop框架对一次方法调用的封装，
            它实现自org.aopalliance.intercept.Joinpoint接口
            平时直接用注解方式定义切面实现环绕通知时，入参是一个org.aspectj.lang.JoinPoint
            类不是完全相同的
         */
        LOGGER.info("代理前，time:{}", DateUtils.now());
        //被代理逻辑
        Object result = invocation.proceed();
        LOGGER.info("代理后，time:{}", DateUtils.now());
        return result;
    }

    @Override
    public boolean implementsInterface(Class<?> intf) {
        return false;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {

    }
}
