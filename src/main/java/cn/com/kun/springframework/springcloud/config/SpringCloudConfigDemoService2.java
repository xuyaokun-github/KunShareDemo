package cn.com.kun.springframework.springcloud.config;

import cn.com.kun.common.utils.JacksonUtils;
import cn.com.kun.config.threadpool.CustomThreadPoolProperties;
import cn.com.kun.controller.spring.SpringCloudConfigDemoController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * 验证配置的动态刷新
 * 验证ConfigurationProperties类
 *
 * author:xuyaokun_kzx
 * date:2021/8/6
 * desc:
*/
@Service
public class SpringCloudConfigDemoService2 {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringCloudConfigDemoController.class);

    @Autowired
    private CustomThreadPoolProperties customThreadPoolProperties;

    @PostConstruct
    public void init() throws IOException {
        int a = 0;
    }

    public String methodCustomThreadPoolProperties(){
        String res = JacksonUtils.toJSONString(customThreadPoolProperties.getItems());
        LOGGER.info("customThreadPoolProperties:{}", res);
        return res;
    }

}
