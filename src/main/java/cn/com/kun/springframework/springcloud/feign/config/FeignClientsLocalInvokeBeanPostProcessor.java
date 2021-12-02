package cn.com.kun.springframework.springcloud.feign.config;

import cn.com.kun.framework.quartz.common.AutoJobRegisterConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Feign bean扩展
 * 支持通过开关控制是否启用本地调试
 * 原理：设置了url,feign就不会再通过注册中心的方式找目标地址
 *
 * author:xuyaokun_kzx
 * date:2021/11/17
 * desc:
*/
public class FeignClientsLocalInvokeBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware, EnvironmentAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoJobRegisterConfig.class);

    private ApplicationContext applicationContext;

    private Environment environment;

    private AtomicInteger atomicInteger = new AtomicInteger();

    //工厂bean的class类型：org.springframework.cloud.openfeign.FeignClientFactoryBean
    private String beanNameOfFeignClientFactoryBean = "org.springframework.cloud.openfeign.FeignClientFactoryBean";

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        //计数器保证启动过程里只执行一次
        if(atomicInteger.getAndIncrement() == 0){
            Class beanNameClz = null;
            try {
                beanNameClz = Class.forName(beanNameOfFeignClientFactoryBean);
                Class finalBeanNameClz = beanNameClz;
                Map<String, Object> beans = applicationContext.getBeansOfType(beanNameClz);
                if (beans != null){
                    beans.forEach((feignBeanName, beanOfFeignClientFactoryBean)->{
                        try {
                            changeUrl(finalBeanNameClz, beanOfFeignClientFactoryBean);
                        } catch (Exception e) {
                            LOGGER.error("FeignClientsLocalInvokeBeanPostProcessor error", e);
                        }
                    });
                }
            } catch (Exception e) {
                LOGGER.error("FeignClientsLocalInvokeBeanPostProcessor error", e);
            }

        }


        return null;
    }

    /**
     * 原理是默认情况下是url属性是空值，将它改成本地指定的地址，就不会走注册中心
     * @param clazz
     * @param obj
     * @throws Exception
     */
    private  void changeUrl(Class clazz, Object obj) throws Exception{

        Field nameField = ReflectionUtils.findField(clazz, "name");
        if(Objects.nonNull(nameField)){
            ReflectionUtils.makeAccessible(nameField);
            String name = (String) nameField.get(obj);
            if (StringUtils.isNotEmpty(name)){
                Field field = ReflectionUtils.findField(clazz, "url");
                if(Objects.nonNull(field)){
                    ReflectionUtils.makeAccessible(field);
                    //旧url
                    Object value = field.get(obj);
                    String newUrl = getLocalInvokeUrlFromConfig(name);
                    if (StringUtils.isNotEmpty(newUrl)){
                        //假如配置了本地调用url,则替换
                        ReflectionUtils.setField(field, obj, newUrl);
                        LOGGER.info("feignBeanName：[{}], 采用本地调试url:[{}]", name, newUrl);
                    }
                }
            }
        }
    }

    private String getLocalInvokeUrlFromConfig(String name) {
        //例子：feign.localinvoke.url.kunwebdemo=http://127.0.0.1:8091
        return environment.getProperty(String.format("feign.localinvoke.url.%s", name));
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
