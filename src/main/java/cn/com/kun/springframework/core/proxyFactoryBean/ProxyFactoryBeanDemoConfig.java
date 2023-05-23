package cn.com.kun.springframework.core.proxyFactoryBean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Nullable;
import java.lang.reflect.Method;


@Configuration
public class ProxyFactoryBeanDemoConfig {

    //注册目标对象(被代理对象)
    @Bean
    public ProxyFactoryBeanDemoService proxyFactoryBeanDemoService() {
        return new ProxyFactoryBeanDemoService();
    }

    //注册一个前置通知
    @Bean
    public MethodBeforeAdvice beforeAdvice() {
        MethodBeforeAdvice advice = new MethodBeforeAdvice() {
            @Override
            public void before(Method method, Object[] args, @Nullable Object target) throws Throwable {
                System.out.println("准备调用：" + method);
            }
        };
        return advice;
    }

    /*
        MethodBeforeAdvice 和 MethodInterceptor 都应该放入容器里
     */
    //注册一个后置通知
    @Bean
    public MethodInterceptor costTimeInterceptor() {
        MethodInterceptor methodInterceptor = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                long starTime = System.nanoTime();
                Object result = invocation.proceed();
                long endTime = System.nanoTime();
                System.out.println(invocation.getMethod() + ",耗时(纳秒)：" + (endTime - starTime));
                return result;
            }
        };
        return methodInterceptor;
    }

    //注册ProxyFactoryBean
    @Bean
    public ProxyFactoryBean proxyFactoryBeanDemoServiceProxy() {
        //1.创建ProxyFactoryBean
        ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
        //2.设置目标对象的bean名称
        proxyFactoryBean.setTargetName("proxyFactoryBeanDemoService");
        //3.设置拦截器的bean名称列表，此处2个（advice1和advice2)
        proxyFactoryBean.setInterceptorNames("beforeAdvice", "costTimeInterceptor");
        return proxyFactoryBean;
    }


}