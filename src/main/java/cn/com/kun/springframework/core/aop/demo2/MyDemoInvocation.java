package cn.com.kun.springframework.core.aop.demo2;

import java.util.List;

/**
 * 简单理解 ReflectiveMethodInvocation
 * author:xuyaokun_kzx
 * date:2021/12/31
 * desc:
*/
public class MyDemoInvocation {

    private TargetService targetService;
    private List<AopDemoInterceptor> interceptorList;

    private int currentIndex = -1;

    public MyDemoInvocation(TargetService targetService, List<AopDemoInterceptor> interceptorList) {
        this.targetService = targetService;
        this.interceptorList = interceptorList;
    }

    public void process(){

        if (currentIndex == interceptorList.size() -1){
            //假如已经遍历完了，说明全部拦截器都过了一遍，这会可以执行 被代理方法了
            targetService.doService();
            //这里必须return
            return;
        }

        //假如还没遍历完，就继续往下，遍历拦截器
        AopDemoInterceptor aopDemoInterceptor = interceptorList.get(++currentIndex);
        aopDemoInterceptor.doIntercept(this);
    }
}
