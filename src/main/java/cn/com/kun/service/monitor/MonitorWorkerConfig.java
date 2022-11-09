package cn.com.kun.service.monitor;

import cn.com.kun.component.monitor.MonitorManager;
import cn.com.kun.component.monitor.model.MonitorTask;
import cn.com.kun.component.monitor.worker.MonitorWorker;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Configuration
public class MonitorWorkerConfig implements ApplicationContextAware {

    // ---------------------- applicationContext ----------------------
    private static ApplicationContext applicationContext;

    @Autowired
    MonitorManager monitorManager;

    @PostConstruct
    public void init(){

        //两秒执行一次（第一种注册方法）
//        MonitorTask monitorTask = new MonitorTask("Fisrt_monitor");
//        monitorTask.setTimePeriod(2);
//        monitorTask.setTimeUnit(TimeUnit.SECONDS);
//        monitorTask.setMonitorWorker((MonitorWorker) applicationContext.getBean("myFirstMonitorWorker"));
//        monitorManager.addMonitorTask(monitorTask);

        //两秒执行一次（第2种注册方法）不推荐
        MonitorTask monitorTask2 = new MonitorTask("Fisrt_monitor");
        monitorTask2.setTimePeriod(30);
        monitorTask2.setTimeUnit(TimeUnit.SECONDS);
        monitorTask2.setMonitorWorker((MonitorWorker) applicationContext.getBean("mySecondMonitorWorker"));
        monitorManager.addMonitorTask(monitorTask2);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
