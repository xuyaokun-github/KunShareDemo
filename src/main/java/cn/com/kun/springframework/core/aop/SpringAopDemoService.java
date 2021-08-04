package cn.com.kun.springframework.core.aop;

import cn.com.kun.common.annotation.MyAnno1;
import cn.com.kun.common.annotation.MyAnno2;
import org.springframework.stereotype.Service;

@Service
public class SpringAopDemoService {

    @MyAnno1
    @MyAnno2
    public String method(){
        return "target method";
    }

}
