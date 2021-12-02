package cn.com.kun.framework.quartz.demo1;

import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * QuartzJobBean的子类，就是具体要执行的任务
 *
 * Created by xuyaokun On 2020/6/2 23:08
 * @desc:
 */
public class MyFirstQuartzJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                + " 执行MyFirstQuartzJob" + Thread.currentThread().getName());
        System.out.println("Hi! :" + jobExecutionContext.getJobDetail().getKey());
    }
}
