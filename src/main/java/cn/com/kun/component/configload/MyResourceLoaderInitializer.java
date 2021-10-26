package cn.com.kun.component.configload;

import cn.com.kun.common.utils.ThreadUtils;
import cn.com.kun.springframework.core.applicationContextInitializer.ApplicationContextInitializerDemoBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

/**
 * 调整优化环境变量，对于boot框架会默认覆盖一些环境变量，此时我们需要在processor中执行
 * 我们不再需要使用单独的yml文件来解决此问题。
 * 原则：
 * * 1）所有设置为系统属性的，初衷为"对系统管理员可见"、"对外部接入组件可见"（比如starter或者日志组件等）
 * * 2）对设置为lastSource，表示"当用户没有通过yml"配置选项时的默认值--担保策略。**/
public class MyResourceLoaderInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyResourceLoaderInitializer.class);

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        LOGGER.info(ThreadUtils.getCurrentInvokeClassAndMethod());
        //获取运行时环境
        Environment environment = applicationContext.getEnvironment();
        String configPath = environment.getProperty("CONF_PATH");
        if (configPath == null) {
            configPath = environment.getProperty("config.path");
        }

        //添加一个加载配置文件的实现
        applicationContext.addProtocolResolver(new MyXPathProtocolResolver(configPath));

        /*
            可以在ApplicationContextInitializer执行时，向bean工厂注册单例bean
            但是这样做比较少见。
         */
        applicationContext.getBeanFactory().registerSingleton("applicationContextInitializerDemoBean", new ApplicationContextInitializerDemoBean());
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 100;
    }
}