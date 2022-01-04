package cn.com.kun.springframework.core.aop.demo2;

import java.util.ArrayList;
import java.util.List;

public class TestMyDemoInvocation {

    public static void main(String[] args) {

        TargetService targetService = new TargetService();
        List<AopDemoInterceptor> interceptorList = new ArrayList<>();
        interceptorList.add(new AfterInterceptor(targetService));
        interceptorList.add(new AfterInterceptor(targetService));
        interceptorList.add(new BeforeInterceptor(targetService));
        interceptorList.add(new BeforeInterceptor(targetService));
        interceptorList.add(new BeforeInterceptor(targetService));

        /*
            拦截器的注册顺序没关系，最后一定是：
            exec before doIntercept
            exec before doIntercept
            exec before doIntercept
            exec target service
            exec after doIntercept
            exec after doIntercept
         */
        MyDemoInvocation myDemoInvocation = new MyDemoInvocation(targetService, interceptorList);
        myDemoInvocation.process();
    }

}
