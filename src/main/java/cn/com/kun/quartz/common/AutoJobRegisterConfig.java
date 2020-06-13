package cn.com.kun.quartz.common;

import cn.com.kun.quartz.mapper.CustomQuartzJobMapper;
import com.alibaba.fastjson.JSONObject;
import org.quartz.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;

/**
 * 自动任务注册配置
 * 加载数据里配置好的定时任务，然后放入到容器里
 *
 * Created by xuyaokun On 2020/6/3 23:30
 * @desc:
 */
@Component
public class AutoJobRegisterConfig implements BeanFactoryAware {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    //数据库层
    @Autowired
    private CustomQuartzJobMapper customQuartzJobMapper;

    private DefaultListableBeanFactory myListableBeanFactory;

    private final String JOB_ENABLED_FLAG = "Y";

    @PostConstruct
    private void init() throws SchedulerException {

        Scheduler scheduler = null;
        if (schedulerFactoryBean != null){
            System.out.println("获取到quartz调度工厂" + schedulerFactoryBean.getScheduler().getSchedulerName());
            scheduler = schedulerFactoryBean.getScheduler();
            System.out.println("Scheduler-getTypeName:" + scheduler.getClass().getTypeName());
            //  schedulerFactoryBean.stop();
//            schedulerFactoryBean.getScheduler().unscheduleJob();
        }
        //加载任务表
        List<CustomQuartzJob> customQuartzJobList = customQuartzJobMapper.query(null);
        //封装成Job,放入容器
        Scheduler finalScheduler = scheduler;
        customQuartzJobList.forEach(customQuartzJob -> {
            if (JOB_ENABLED_FLAG.equals(customQuartzJob.getEnabled())){
                System.out.println("AutoJobRegisterConfig加载到CustomQuartzJob：" + customQuartzJob.getJobName());;
                try {
                    Class clazz = Class.forName(customQuartzJob.getJobClass());
                    RootBeanDefinition beanDefinition = new RootBeanDefinition(clazz);
                    //设置成单例
                    beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
                    //注意这里放入的bean在容器中是单例的
                    JobDetail jobDetail = buildJobDetail(customQuartzJob);
                    //注册JobDetail
                    myListableBeanFactory.registerSingleton(customQuartzJob.getJobName(), jobDetail);
                    //注册触发器
                    Trigger trigger = buildTrigger(customQuartzJob, jobDetail);
                    myListableBeanFactory.registerSingleton(customQuartzJob.getTriggerName(), trigger);
                    //开启调度
                    /**
                     * 注意这里必须手动开启调度
                     * 因为可能之前曾经设置为N,被禁止调度，假如不开启，这个触发器还是被禁止的状态
                     */
                    finalScheduler.scheduleJob(trigger);

                } catch (Exception e) {
                    System.out.println("AutoJobRegisterConfig出现异常，JobClass：" + customQuartzJob.getJobClass());
                    e.printStackTrace();
                }

            }else {
                System.out.println(String.format("任务【%s】未启用，不加载！", customQuartzJob.getJobName()));
                try {
                    //假如未启用，禁用调度
                    //假如不主动禁止调度，quartz会根据之前数据库表的状态来进行调度
                    //所以为了让任务不执行，必须主动设置一下不进行调度
                    finalScheduler.unscheduleJob(new TriggerKey(customQuartzJob.getTriggerName(), customQuartzJob.getTriggerGroupName()));
                } catch (SchedulerException e) {
                    System.out.println("禁用调度异常");
                    e.printStackTrace();
                }
            }

        });

    }

    //记录下BeanFactory的引用，为了后续将bean放进容器
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory)beanFactory;
        this.myListableBeanFactory = listableBeanFactory;
    }

    /**
     * 构建JobDetail
     *
     * @param customQuartzJob
     * @return
     */
    private JobDetail buildJobDetail(CustomQuartzJob customQuartzJob){
        JobDetail jobDetail = null;
        try {
            Class clazz = Class.forName(customQuartzJob.getJobClass());
            JobBuilder jobBuilder = JobBuilder.newJob(clazz)
                    .withIdentity(customQuartzJob.getJobName(), customQuartzJob.getGroupName());

            if (!StringUtils.isEmpty(customQuartzJob.getJobParam())){
                JSONObject jsonObject = JSONObject.parseObject(customQuartzJob.getJobParam());
                Set<String> set = jsonObject.keySet();
                for (String key : set){
                    jobBuilder.usingJobData(key, (String) jsonObject.get(key));
                }
            }
                    //JobDataMap可以给任务execute传递参数
            jobDetail = jobBuilder.storeDurably().build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return jobDetail;
    }

    /**
     * 构建触发器
     *
     * @param customQuartzJob
     * @param jobDetail
     * @return
     */
    public Trigger buildTrigger(CustomQuartzJob customQuartzJob, JobDetail jobDetail){
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger()
                .forJob(jobDetail) //指定对应的JobDetail
                .withIdentity(customQuartzJob.getTriggerName(), customQuartzJob.getTriggerGroupName());

        if (!StringUtils.isEmpty(customQuartzJob.getTriggerParam())){
            JSONObject jsonObject = JSONObject.parseObject(customQuartzJob.getTriggerParam());
            Set<String> set = jsonObject.keySet();
            for (String key : set){
                triggerBuilder.usingJobData(key, (String) jsonObject.get(key));
            }
        }
        Trigger trigger = triggerBuilder.startNow()
                //.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
                .withSchedule(CronScheduleBuilder.cronSchedule(customQuartzJob.getCron()))
                .build();
        return trigger;
    }
}
