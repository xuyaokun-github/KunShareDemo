package cn.com.kun.schedule.xxljob.service;

import cn.com.kun.schedule.xxljob.handler.ExecTemplateJobHandler;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Service;

@Service
public class XxlJobExtendService implements BeanFactoryAware {

    private Logger LOGGER = LoggerFactory.getLogger(ExecTemplateJobHandler.class);

    private DefaultListableBeanFactory myListableBeanFactory;

    /**
     * 注册bean
     * @param executorHandlerName
     */
    public void register(String executorHandlerName){
        /*
            添加进spring容器之后，bean就已经可用，但要考虑一个问题：重启之后，必须重新添加bean
            每次重启都要把所有定时任务对应的bean定义好放进容器，每个pod都需要执行该步骤
         */
        RootBeanDefinition beanDefinition = new RootBeanDefinition(ExecTemplateJobHandler.class);
        //设置成单例
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        //注意这里放入的bean在容器中是单例的
        //反射构建实例或者直接new
        //注册bean定义
        myListableBeanFactory.registerBeanDefinition(executorHandlerName, beanDefinition);
        //主动调用registerSingleton，有一个缺点，无法自动解析注解
//        ExecTemplateJobHandler templateJobHandler = new ExecTemplateJobHandler(executorHandlerName);
//        myListableBeanFactory.registerSingleton(executorHandlerName, templateJobHandler);
        LOGGER.info("向spring容器注册bean:{} 成功", executorHandlerName);
        //是否可以不放入spring容器？其实是可以的，但是假如不放入容器，就无法在类上用注入特性

        //如何做到放入容器，同时也能支持类上的注解解析，正确的姿势是让spring来创建这个bean
        ExecTemplateJobHandler templateJobHandler = (ExecTemplateJobHandler) myListableBeanFactory.getBean(executorHandlerName);

        XxlJobSpringExecutor.registJobHandler(executorHandlerName, templateJobHandler);
        LOGGER.info("向XxlJobSpringExecutor容器注册bean:{} 成功", executorHandlerName);

    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory)beanFactory;
        this.myListableBeanFactory = listableBeanFactory;
    }


}
