package cn.com.kun.framework.quartz.demo1;

import org.quartz.*;
import org.springframework.context.annotation.Bean;

/**
 * Quartz的定义
 *
 * Created by xuyaokun On 2020/6/2 23:14
 * @desc:
 */
//@Configuration
public class MyQuartzConfig {

    /**
     * 定义一个JobDetail（基于一个具体的Job对象得到一个JobDetail）
     * JobDetail表示一个具体可执行的调度程序
     * @return
     */
    @Bean
    public JobDetail myJobDetail(){
        JobDetail jobDetail = null;
        try {
            Class clazz = Class.forName("cn.com.kun.framework.quartz.demo1.MyFirstQuartzJob");
            jobDetail = JobBuilder.newJob(clazz)
                    .withIdentity("myJob1", "myJobGroup1")
                    .requestRecovery(true)
                    //JobDataMap可以给任务execute传递参数
                    .usingJobData("job_param", "job_param1")
                    .storeDurably()
                    .build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return jobDetail;
    }

    /**
     * 定义触发器
     *
     * @return
     */
    @Bean
    public Trigger myTrigger(){
        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(myJobDetail()) //指定对应的JobDetail
                .withIdentity("myTrigger1", "myTriggerGroup1")
                .usingJobData("job_trigger_param", "job_trigger_param1")
                .startNow()
                //.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
                .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * * * ? 2020"))
                .build();
        return trigger;
    }


}