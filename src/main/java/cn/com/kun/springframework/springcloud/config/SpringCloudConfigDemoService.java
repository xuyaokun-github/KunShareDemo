package cn.com.kun.springframework.springcloud.config;

import cn.com.kun.controller.spring.SpringCloudConfigDemoController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

/**
 * 验证配置的动态刷新
 * 验证普通的@Value
 *
 * author:xuyaokun_kzx
 * date:2021/8/6
 * desc:
*/
@RefreshScope //标记需要动态刷新
@Service
public class SpringCloudConfigDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(SpringCloudConfigDemoController.class);

    @Value("${nbaplay.level}")
    private String nbaplayLevel;

    @PostConstruct
    public void init() throws IOException {
        int a = 0;
    }

    public String method(){
        LOGGER.info("nbaplayLevel:{}", nbaplayLevel);
        return nbaplayLevel;
    }


}
