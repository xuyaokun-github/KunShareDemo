package cn.com.kun.framework.quartz.job;

import cn.com.kun.framework.quartz.service.MyQuartzLogicService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * QuartzJobBean的子类，就是具体要执行的任务
 * 继承QuartzJobBean和实现Job都可以
 *
 * Created by xuyaokun On 2020/6/2 23:08
 * @desc:
 */
@DisallowConcurrentExecution
public class MySecondCustomQuartzJob /*extends QuartzJobBean*/ implements Job {

    private String name;

    @Value("${spring.application.name}")
    private String applicationName;

    //注入其他服务层
    @Autowired
    private MyQuartzLogicService myQuartzLogicService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + " " + this.getName() + " " + getApplicationName()
                + " ===================执行MySecondCustomQuartzJob start" + Thread.currentThread().getName());
//        System.out.println("Hi! :" + jobExecutionContext.getJobDetail().getKey());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myQuartzLogicService.show();
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + " " + this.getName() + " " + getApplicationName()
                + " ===================执行MySecondCustomQuartzJob end" + Thread.currentThread().getName());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
