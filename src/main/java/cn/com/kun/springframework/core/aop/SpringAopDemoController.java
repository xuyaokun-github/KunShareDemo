package cn.com.kun.springframework.core.aop;

import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.core.aop.abstractPointcutAdvisorDemo.SpringAopTimeLogDemoService;
import cn.com.kun.springframework.core.aop.demo1.AopProxyUtilsDemo;
import cn.com.kun.springframework.core.aop.service.SpringAopDemoService;
import cn.com.kun.springframework.core.proxyFactoryBean.ProxyFactoryBeanDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RequestMapping("/spring-aop-demo")
@RestController
public class SpringAopDemoController {

    private final static Logger logger = LoggerFactory.getLogger(SpringAopDemoController.class);

    @Autowired
    private SpringAopDemoService springAopDemoService;

    @Autowired
    AopProxyUtilsDemo aopProxyUtilsDemo;

//    @Autowired
    private SpringAopTimeLogDemoService springAopTimeLogDemoService;


    @Autowired
    @Qualifier("proxyFactoryBeanDemoServiceProxy")
    ProxyFactoryBeanDemoService proxyFactoryBeanDemoService;

    @Autowired
    @Qualifier("proxyFactoryBeanDemoService")
    ProxyFactoryBeanDemoService proxyFactoryBeanDemoServiceSource;

    @PostConstruct
    public void init(){
//        logger.info("");
    }

    @GetMapping("/testAop")
    public ResultVo testAop(){

        springAopDemoService.method();
        return ResultVo.valueOfSuccess(null);
    }


    /**
     * 验证通过ApplicationContextInitializer注册单例bean
     * @return
     */
    @GetMapping("/testAopProxyUtilsDemo")
    public ResultVo testAopProxyUtilsDemo(){

        aopProxyUtilsDemo.method();
        return ResultVo.valueOfSuccess(null);
    }

    /**
     * 非注解方式定义切面
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/testTimeLog")
    public ResultVo testTimeLog() throws InterruptedException {

        springAopTimeLogDemoService.method();
        return ResultVo.valueOfSuccess("");
    }


    @GetMapping("/testProxyFactoryBean")
    public ResultVo testProxyFactoryBean() throws InterruptedException {

        proxyFactoryBeanDemoService.m1();
        proxyFactoryBeanDemoService.m2();

        proxyFactoryBeanDemoServiceSource.m1();
        proxyFactoryBeanDemoServiceSource.m2();
        return ResultVo.valueOfSuccess("");
    }
}
