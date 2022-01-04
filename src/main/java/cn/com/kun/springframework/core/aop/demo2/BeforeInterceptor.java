package cn.com.kun.springframework.core.aop.demo2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeforeInterceptor implements AopDemoInterceptor{

    private final static Logger LOGGER = LoggerFactory.getLogger(BeforeInterceptor.class);


    public BeforeInterceptor(TargetService targetService) {
    }

    @Override
    public void doIntercept(MyDemoInvocation myDemoInvocation) {

        //
//        LOGGER.info("exec before doIntercept");

        /*
            假如这里进到的是
         */
        System.out.println("exec before doIntercept");
        myDemoInvocation.process();
    }

}
