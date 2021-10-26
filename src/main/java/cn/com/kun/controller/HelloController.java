package cn.com.kun.controller;

import cn.com.kun.common.utils.SpringContextUtil;
import cn.com.kun.common.vo.people.People;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@RestController
public class HelloController {

    private final static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${nexusdemo:123}")
    private String nexusdemo;

    @PostConstruct
    public void init() throws IOException {
        logger.info(nexusdemo);
        ApplicationContext applicationContext = SpringContextUtil.getContext();
        Resource resource = applicationContext.getResource("classpath:config.txt");
        File file = resource.getFile();
        InputStream inputStream = resource.getInputStream();
        logger.info(IOUtils.toString(inputStream, Charset.forName("UTF-8")));
        logger.info(applicationContext.getApplicationName());
    }

    @RequestMapping("/hello")
    public String testString(){
        return "kunghsu";
    }

    @RequestMapping("/testRestTemplate")
    public String testRestTemplate(){

        restTemplate.getForObject("http://127.0.0.1:8081/one/test", String.class);
        return "kunghsu";
    }


    @RequestMapping("/testExclude")
    public People testExclude(){
        People user = new People();
        user.setLastname("xyk");
        user.setFirstname("kunghsu");
        user.setPhone("10086");
        user.setEmail("12306@qq.com");
        int a = 1/0;
        return user;
    }

}
