package cn.com.kun.springframework.core.beanDefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * 这个必须定义成bean 被spring识别
 *
 * @author Kunghsu
 * @datetime 2018年8月4日 下午1:01:19
 * @desc
 */
@Component
public class KunBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor {

    private static Logger LOGGER = LoggerFactory.getLogger(KunBeanDefinitionRegistry.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        LOGGER.info("postProcessBeanFactory() beanDefinition的个数=====>"+beanFactory.getBeanDefinitionCount());

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {

        RootBeanDefinition helloBean = new RootBeanDefinition(BeanDefinitionHelloService.class);
        //设置成单例
        helloBean.setScope(BeanDefinition.SCOPE_SINGLETON);
        //设置bean的属性
        helloBean.getPropertyValues().add("name", "xyk").add("address", "罗湖区");
        //新增Bean定义，将bean注册进spring容器
        registry.registerBeanDefinition("beanDefinitionHelloService", helloBean);

    }

}
