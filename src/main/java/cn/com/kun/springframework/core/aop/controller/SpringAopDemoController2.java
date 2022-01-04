package cn.com.kun.springframework.core.aop.controller;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.core.aop.abstractPointcutAdvisorDemo.SpringAopTimeLogDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/spring-aop-demo2")
@RestController
public class SpringAopDemoController2 {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringAopDemoController2.class);

    @Autowired
    private SpringAopTimeLogDemoService springAopTimeLogDemoService;

    @GetMapping("/testAop")
    public ResultVo testAop(){

        /*
            这里有个很奇怪的问题，假如下面这一句打上断点，将会触发TimeLogIntroductionInterceptor的增强逻辑
            假如不打断点，则不会触发！

            假如把上面的注入关掉，也不会触发。
            很奇怪的一个问题，先记录，后续再深入排查

            我觉得这个是debug的一个bug,暂不深究了
         */
        return ResultVo.valueOfSuccess(null);
    }

}
