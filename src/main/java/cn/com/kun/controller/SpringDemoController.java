package cn.com.kun.controller;

import cn.com.kun.common.utils.SpringContextUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@RequestMapping("/springdemo")
@RestController
public class SpringDemoController {

    public final static Logger logger = LoggerFactory.getLogger(SpringDemoController.class);

    @Autowired
    private SpringContextUtil springContextUtil;

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

}
