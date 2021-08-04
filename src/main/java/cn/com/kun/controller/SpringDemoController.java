package cn.com.kun.controller;

import cn.com.kun.bean.entity.User;
import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.common.utils.SpringContextUtil;
import cn.com.kun.common.vo.ResultVo;
import cn.com.kun.springframework.core.aop.SpringAopDemoService;
import cn.com.kun.springframework.core.aop.demo1.AopProxyUtilsDemo;
import cn.com.kun.springframework.core.applicationContextInitializer.ApplicationContextInitializerDemoBean;
import cn.com.kun.springframework.core.beanDefinition.BeanDefinitionDemoService;
import cn.com.kun.springframework.core.binder.NbaplayBinderDemo;
import cn.com.kun.springframework.core.orderComparator.OrderComparatorDemoServcie;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.LiveBeansView;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import static org.springframework.core.io.support.SpringFactoriesLoader.FACTORIES_RESOURCE_LOCATION;

@RequestMapping("/springdemo")
@RestController
public class SpringDemoController {

    public final static Logger logger = LoggerFactory.getLogger(SpringDemoController.class);

    @Autowired
    private SpringContextUtil springContextUtil;

    @Autowired
    private SpringAopDemoService springAopDemoService;

    @Value("${nbaplay.level}")
    private String nbaplayLevel;

    @Autowired
    BeanDefinitionDemoService beanDefinitionDemoService;

    @Autowired
    NbaplayBinderDemo nbaplayBinderDemo;

    @Autowired
    OrderComparatorDemoServcie orderComparatorDemoServcie;

    @Autowired
    ApplicationContextInitializerDemoBean applicationContextInitializerDemoBean;

    @Autowired
    AopProxyUtilsDemo aopProxyUtilsDemo;

    @PostConstruct
    public void init() throws IOException {
        /**
         * 获取classpath下的文件内容
         * 但这种写法，在文件被打进jar包后会读取失败
         */
        ApplicationContext applicationContext = SpringContextUtil.getContext();
        Resource resource = applicationContext.getResource("classpath:config.txt");
        File file = resource.getFile();
        InputStream inputStream = resource.getInputStream();
        logger.info(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
    }

    /**
     * 分析一个@JsonFormat失效的问题
     * @return
     */
    @GetMapping("/testJsonFormat")
    public ResultVo<User> testJsonFormat(){

        User user = new User();
        user.setCreateTime(new Date());
        return ResultVo.valueOfSuccess(user);
    }

    /**
     * 分析一个@JsonFormat失效的问题
     * @return
     */
    @GetMapping("/testJsonFormat2")
    public ResultVo<List<User>> testJsonFormat2(){

        User user = new User();
        user.setCreateTime(new Date());
        List<User> userList = new ArrayList<>();
        userList.add(user);
        return ResultVo.valueOfSuccess(userList);
    }

    @GetMapping("/testAop")
    public ResultVo testAop(){

        springAopDemoService.method();
        return ResultVo.valueOfSuccess(null);
    }

    @GetMapping("/testBeanDefinition")
    public ResultVo testBeanDefinition(){

        beanDefinitionDemoService.method();
        return ResultVo.valueOfSuccess(null);
    }

    @GetMapping("/testShowAllFactories")
    public ResultVo testShowAllFactories(){

        //    spring.factories文件

        List<String> stringList = SpringFactoriesLoader.loadFactoryNames(ApplicationContextInitializer.class, Thread.currentThread().getContextClassLoader());
        logger.info("SpringFactoriesLoader.loadFactoryNames:{}", JacksonUtils.toJSONString(stringList));
        return ResultVo.valueOfSuccess(null);
    }

    @GetMapping("/testShowAllFactoriesFileUrl")
    public ResultVo testShowAllFactoriesFileUrl() throws IOException {

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> urls = (classLoader != null ?
                classLoader.getResources(FACTORIES_RESOURCE_LOCATION) :
                ClassLoader.getSystemResources(FACTORIES_RESOURCE_LOCATION));
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            logger.info("URL:{}", url.toString());
        }
        return ResultVo.valueOfSuccess(null);
    }

    @GetMapping("/testBinder")
    public ResultVo testBinder(){

        nbaplayBinderDemo.method();
        return ResultVo.valueOfSuccess(null);
    }

    @GetMapping("/testOrderComparator")
    public ResultVo testOrderComparator(){

        orderComparatorDemoServcie.method();
        return ResultVo.valueOfSuccess(null);
    }

    /**
     * 验证通过ApplicationContextInitializer注册单例bean
     * @return
     */
    @GetMapping("/testApplicationContextInitializerDemoBean")
    public ResultVo testApplicationContextInitializerDemoBean(){

        applicationContextInitializerDemoBean.show();
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

    @GetMapping("/testLiveBeansView")
    public String testLiveBeansView(){

        String res = new LiveBeansView().getSnapshotAsJson();
        return res;
    }



}
