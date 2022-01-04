package cn.com.kun.springframework.core.aop.demo1;

import cn.com.kun.springframework.core.aop.service.SpringAopDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AopProxyUtils demo
 * author:xuyaokun_kzx
 * date:2021/8/3
 * desc:
*/
@Service
public class AopProxyUtilsDemo {

    private final static Logger LOGGER = LoggerFactory.getLogger(AopProxyUtilsDemo.class);

    //SpringAopDemoService是一个被aop代理的类
    @Autowired
    private SpringAopDemoService springAopDemoService;

    public void method(){

        boolean isAopProxy = AopUtils.isAopProxy(springAopDemoService);
        LOGGER.info("是否为aop代理类：{}", isAopProxy);

        //拿到源对象
        Object target = AopProxyUtils.getSingletonTarget(springAopDemoService);
        LOGGER.info("target对象和代理对象是否相等：{}", target == springAopDemoService);

        //用被代理对象调用具体业务方法
        //用源对象执行方法，不会触发aop切面的逻辑，即不会执行代理的逻辑
        LOGGER.info("用被代理对象调用具体业务方法：{}", ((SpringAopDemoService)target).method());

    }

}
