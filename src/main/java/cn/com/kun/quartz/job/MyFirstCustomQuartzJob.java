package cn.com.kun.quartz.job;

import cn.com.kun.quartz.service.MyQuartzLogicService;
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
public class MyFirstCustomQuartzJob /*extends QuartzJobBean*/ implements Job {

    private String name;

    @Value("${spring.application.name}")
    private String applicationName;

    //注入其他服务层
    @Autowired
    private MyQuartzLogicService myQuartzLogicService;
    /*
    INSERT INTO `test`.`tbl_custom_quartz_job` (`job_id`, `job_class`, `job_name`,
    `group_name`, `job_param`, `trigger_name`, `trigger_group_name`,
    `trigger_param`, `cron`, `enabled`)
    VALUES ('1', 'cn.com.kun.quartz.job.MyFirstCustomQuartzJob', 'MyFirstCustomQuartzJob',
     'MyFirstCustomQuartzGroup', '{\'name\':\'kunghsu\'}', 'MyFirstCustomQuartzTrigger',
     'MyFirstCustomQuartzTriggerGroup', '{\'name\':\'kunghsu\'}', '0/10 * * * * ? 2020', 'Y');
     */
//    @Override
//    protected void executeInternal(JobExecutionContext jobExecutionContext) {
//        myQuartzLogicService.show();
//        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//                + " " + this.getName() + " " + getApplicationName()
//                + " ===================执行MyFirstCustomQuartzJob" + Thread.currentThread().getName());
////        System.out.println("Hi! :" + jobExecutionContext.getJobDetail().getKey());
//    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + " " + this.getName() + " " + getApplicationName()
                + " ===================执行MyFirstCustomQuartzJob start" + Thread.currentThread().getName());
//        System.out.println("Hi! :" + jobExecutionContext.getJobDetail().getKey());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myQuartzLogicService.show();
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + " " + this.getName() + " " + getApplicationName()
                + " ===================执行MyFirstCustomQuartzJob end" + Thread.currentThread().getName());
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
