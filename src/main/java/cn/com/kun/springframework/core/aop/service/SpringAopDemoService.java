package cn.com.kun.springframework.core.aop.service;

import cn.com.kun.springframework.core.aop.SpringAopDemoController;
import cn.com.kun.springframework.core.aop.annotation.MyAnno1;
import cn.com.kun.springframework.core.aop.annotation.MyAnno2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SpringAopDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringAopDemoController.class);

    @MyAnno1
    @MyAnno2
    public String method(){
        LOGGER.info("我是被代理类的逻辑");
        return "target method";
    }

}
