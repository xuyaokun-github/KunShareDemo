package cn.com.kun.quartz.common;

import cn.com.kun.quartz.mapper.CustomQuartzJobMapper;
import com.alibaba.fastjson.JSONObject;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(prefix = "kunsharedemo.quartz", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
@Component
public class AutoJobRegisterConfig implements BeanFactoryAware {

    private static final Logger logger = LoggerFactory.getLogger(AutoJobRegisterConfig.class);

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
            logger.info("获取到quartz调度工厂" + schedulerFactoryBean.getScheduler().getSchedulerName());
            scheduler = schedulerFactoryBean.getScheduler();
            logger.info("Scheduler-getTypeName:" + scheduler.getClass().getTypeName());
            //  schedulerFactoryBean.stop();
//            schedulerFactoryBean.getScheduler().unscheduleJob();
        }
        //加载任务表
        List<CustomQuartzJob> customQuartzJobList = customQuartzJobMapper.query(null);
        if (customQuartzJobList == null){
            return;
        }
        //封装成Job,放入容器
        Scheduler finalScheduler = scheduler;
        customQuartzJobList.forEach(customQuartzJob -> {
            if (JOB_ENABLED_FLAG.equals(customQuartzJob.getEnabled())){
                logger.info("AutoJobRegisterConfig加载到CustomQuartzJob：" + customQuartzJob.getJobName());;
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

                    TriggerKey triggerKey = new TriggerKey(customQuartzJob.getTriggerName(), customQuartzJob.getTriggerGroupName());

                    boolean checkExistsTrigger = finalScheduler.checkExists(triggerKey);
                    logger.info("触发器{},存在状态：{}", triggerKey.toString(), checkExistsTrigger);

                    if (!checkExistsTrigger){
                        //假如不存在,要进行手动调度
                        finalScheduler.scheduleJob(trigger);
                    }else {
                        //开启调度
                        /**
                         * 注意这里必须手动开启调度
                         * 因为可能曾经设置为N,被禁止（取消）调度，假如不开启，这个触发器在数据库中还是被禁止的状态
                         * 但是，注意不能调用scheduleJob方法，假如在数据库quartz表中已经是开启状态，再次调scheduleJob方法会抛异常
                         * 所以，最好是调用rescheduleJob方法，作用是重新调度，让它启动
                         */
                        finalScheduler.rescheduleJob(triggerKey, trigger);
                    }

                    //假如一个触发器被误删了，用resumeTrigger方法不会重新添加触发器到数据库
//                    finalScheduler.resumeTrigger(triggerKey);

                } catch (Exception e) {
                    logger.error("AutoJobRegisterConfig出现异常，JobClass：" + customQuartzJob.getJobClass(), e);
                }

            }else {
                logger.info(String.format("任务【%s】未启用，不加载！", customQuartzJob.getJobName()));
                try {
                    //假如未启用，禁用调度
                    //假如不主动禁止调度，quartz会根据之前数据库表的状态来进行调度
                    //所以为了让任务不执行，必须主动设置一下不进行调度
                    finalScheduler.unscheduleJob(new TriggerKey(customQuartzJob.getTriggerName(), customQuartzJob.getTriggerGroupName()));
                } catch (SchedulerException e) {
                    logger.error("禁用调度异常", e);
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
            logger.error("buildJobDetail异常", e);
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
