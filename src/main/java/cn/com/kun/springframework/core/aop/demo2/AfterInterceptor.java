package cn.com.kun.springframework.core.aop.demo2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AfterInterceptor implements AopDemoInterceptor{

    private final static Logger LOGGER = LoggerFactory.getLogger(AfterInterceptor.class);

    public AfterInterceptor(TargetService targetService) {

    }

    @Override
    public void doIntercept(MyDemoInvocation myDemoInvocation) {

        //
        myDemoInvocation.process();
        System.out.println("exec after doIntercept");

    }

}
