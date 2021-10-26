package cn.com.kun.springframework.core.applicationContextInitializer;

import cn.com.kun.common.utils.ThreadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationContextInitializerDemoBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(ApplicationContextInitializerDemoBean.class);

    private String name;

    public void show(){
        LOGGER.info(ThreadUtils.getCurrentInvokeClassAndMethod());
    }

}
