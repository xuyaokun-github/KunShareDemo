package cn.com.kun.component.scc.configrefresh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.config.client.ConfigServicePropertySourceLocator;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 配置中心主动拉取
 *
 * author:xuyaokun_kzx
 * date:2021/8/6
 * desc:
*/
@Component
public class SccClientConfigFetcher {

    private final static Logger LOGGER = LoggerFactory.getLogger(SccClientConfigFetcher.class);

    @Autowired
    private Environment environment;

    /**
     * 这个是spring cloud config的类
     */
    @Autowired(required = false)
    private ConfigServicePropertySourceLocator configServicePropertySourceLocator;



    public void fetch(){

        PropertySource propertySource = configServicePropertySourceLocator.locate(environment);

        LOGGER.info("配置中心配置版本：{}", propertySource.getProperty("config.client.version"));
        LOGGER.info("nbaplay.level：{}", propertySource.getProperty("nbaplay.level"));
    }

}
