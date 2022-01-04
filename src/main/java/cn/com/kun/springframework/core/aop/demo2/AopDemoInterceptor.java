package cn.com.kun.springframework.core.aop.demo2;

/**
 * 递归demo理解
 */
public interface AopDemoInterceptor {

    void doIntercept(MyDemoInvocation myDemoInvocation);

}
