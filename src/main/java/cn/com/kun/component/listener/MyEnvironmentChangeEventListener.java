package cn.com.kun.component.listener;

import cn.com.kun.common.utils.SpringContextUtil;
import cn.com.kun.config.threadpool.CustomThreadPoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 响应EnvironmentChangeEvent事件
 *
 * author:xuyaokun_kzx
 * date:2021/8/9
 * desc:
*/
@Component
public class MyEnvironmentChangeEventListener implements ApplicationListener<EnvironmentChangeEvent> {

    private final static Logger LOGGER = LoggerFactory.getLogger(MyEnvironmentChangeEventListener.class);

    @Autowired
    CustomThreadPoolProperties customThreadPoolProperties;

//    @Autowired
//    ConfigurationPropertiesRebinder configurationPropertiesRebinder;

    @Override
    public void onApplicationEvent(EnvironmentChangeEvent event) {

        /*
            注意，这里虽然响应了EnvironmentChangeEvent事件，但是CustomThreadPoolProperties内容却还是旧的
            虽然拉取了最新的配置回来，通过event.getKeys()可以知道哪些key发生了变更，但此时bean的属性还没刷新
            因为刷新容器里的bean这个动作还没开始执行。

            原因：
            解决办法：
            第一种
            主动调用org.springframework.cloud.context.properties.ConfigurationPropertiesRebinder.rebind(java.lang.String)
            第二种
            修改类的优先级顺序（但是ConfigurationPropertiesRebinder的优先级已经是最低，不方便改）
            我更推荐第一种
         */
        //这里传入的beanName，而不是属性的前缀
        ConfigurationPropertiesRebinder configurationPropertiesRebinder = SpringContextUtil.getBean("configurationPropertiesRebinder");
        configurationPropertiesRebinder.rebind("customThreadPoolProperties");
        LOGGER.info("开始响应EnvironmentChangeEvent事件，收到的keys:{}, 最新的线程池配置：{}", event.getKeys(), customThreadPoolProperties.toString());

        //在这个类里，可以做一些刷新后的后置处理逻辑，每当触发/refresh接口的逻辑之后就会执行这个监听器的逻辑

    }


}
