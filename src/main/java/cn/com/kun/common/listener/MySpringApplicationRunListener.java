package cn.com.kun.common.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.stereotype.Component;

/**
 * 可以使用该监听器，诊断启动耗时
 * 必须放到resources 文件下的 META-INF/spring.factories   才能生效
 *
 * author:xuyaokun_kzx
 * date:2023/3/31
 * desc:
*/
@Component
public class MySpringApplicationRunListener implements SpringApplicationRunListener {

    private final static Logger LOGGER = LoggerFactory.getLogger(MySpringApplicationRunListener.class);

    @Override
    public void starting() {
        LOGGER.info("MySpringApplicationRunListener starting");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {

    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void started(ConfigurableApplicationContext context) {

    }

    @Override
    public void running(ConfigurableApplicationContext context) {

    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {

    }
}
