package cn.com.kun.springframework.core.aop.service.impl;

import cn.com.kun.springframework.core.aop.annotation.MyAnno1;
import cn.com.kun.springframework.core.aop.annotation.MyAnno2;
import cn.com.kun.springframework.core.aop.service.SpringAopDemoService3;
import org.springframework.stereotype.Service;

@Service
public class SpringAopDemoService3Impl implements SpringAopDemoService3 {

    @MyAnno1
    @MyAnno2
    @Override
    public String method() {

        return "target method";
    }

}
