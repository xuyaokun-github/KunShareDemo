package cn.com.kun.component.monitor;

import cn.com.kun.component.monitor.annotation.MonitorClass;
import cn.com.kun.component.monitor.executor.MonitorExecutor;
import cn.com.kun.component.monitor.model.MonitorTask;
import cn.com.kun.component.monitor.worker.MonitorWorker;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.Executors;

/**
 * 监控（定时检测）组件
 *
 * author:xuyaokun_kzx
 * date:2021/11/29
 * desc:
*/
@Component
public class MonitorManager implements ApplicationContextAware, InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(MonitorManager.class);
    private ApplicationContext applicationContext;

    /**
     * 监控组件初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        this.initMonitorWorkerRepository(applicationContext);
        //默认使用的队列是java.util.concurrent.LinkedBlockingQueue
        MonitorExecutor.setExecutor(Executors.newFixedThreadPool(5,
                new ThreadFactoryBuilder().setDaemon(true).setNameFormat("MonitorExecutor-Thread-" + "%d").build()));
        MonitorExecutor.startExecutor();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void initMonitorWorkerRepository(ApplicationContext applicationContext) {

        if (applicationContext == null) {
            return;
        }
        String[] beanDefinitionNames = applicationContext.getBeanNamesForAnnotation(MonitorClass.class);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);
            MonitorClass monitorClass = AnnotationUtils.findAnnotation(bean.getClass(), MonitorClass.class);
            registerMonitorWorker(monitorClass.bizType(), (MonitorWorker) bean);
            MonitorTask monitorTask = new MonitorTask(monitorClass.bizType());
            monitorTask.setTimePeriod(monitorClass.timePeriod());
            monitorTask.setTimeUnit(monitorClass.timeUnit());
            //用MonitorClass方式注册的，默认注册一个MonitorTask
            addMonitorTask(monitorTask);
        }
    }



    //添加监控任务
    public String addMonitorTask(MonitorTask monitorTask){


        //监控任务放入队列
        if (StringUtils.isEmpty(monitorTask.getMonitorTaskId())){
            //生成监控任务ID
            String monitorTaskId = UUID.randomUUID().toString();
            monitorTask.setMonitorTaskId(monitorTaskId);
        }

        MonitorExecutor.offerTask(monitorTask);

        //返回监控任务ID
        return monitorTask.getMonitorTaskId();
    }

    /**
     * 假如监控逻辑是不依赖参数的，可以用这种方法注册
     * 只需注册一次，简化编程时的调用
     * @param bizType
     * @param monitorWorker
     */
    public void registerMonitorWorker(String bizType, MonitorWorker monitorWorker){
        MonitorExecutor.registerMonitorWorker(bizType, monitorWorker);
    }

    /**
     * 这个方法有没有必要呢？取消已经放入线程池里的job
     * @param monitorTaskId
     */
    public void cancel(String monitorTaskId){

        //取消监控
        MonitorExecutor.cancelMonitorTask(monitorTaskId);
    }

}
