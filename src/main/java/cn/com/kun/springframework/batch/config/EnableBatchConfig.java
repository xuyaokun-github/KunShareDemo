package cn.com.kun.springframework.batch.config;


import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

//是否开启批处理机制
@EnableBatchProcessing
@Configuration
public class EnableBatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobRegistry jobRegistry;

    //自定义JobLauncher
    @Bean
    public JobLauncher customJobLauncher() {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(jobLauncherTaskExecutor());
        return jobLauncher;
    }

    @Bean
    public TaskExecutor jobLauncherTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(2);
        taskExecutor.setQueueCapacity(0);
        taskExecutor.setThreadNamePrefix("jobLauncherTaskExecutor-Thread-");
        return taskExecutor;
    }

    /**
     * 任务续跑依赖该定义（spring-batch框架依赖，需要手动添加进来）
     * @return
     */
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor postProcessor = new JobRegistryBeanPostProcessor();
        postProcessor.setJobRegistry(jobRegistry);
        return postProcessor;
    }


}
