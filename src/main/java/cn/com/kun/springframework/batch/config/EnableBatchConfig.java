package cn.com.kun.springframework.batch.config;


import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.ExecutionContextSerializer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.dao.DefaultExecutionContextSerializer;
import org.springframework.batch.core.repository.dao.Jackson2ExecutionContextStringSerializer;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

//是否开启批处理机制
@EnableBatchProcessing
@Configuration
public class EnableBatchConfig {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobRegistry jobRegistry;

//    @Autowired
//    private JobExplorer jobExplorer;

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


    @Bean
    public ExecutionContextSerializer defaultExecutionContextSerializer(){
        return new DefaultExecutionContextSerializer();
    }

    /**
     *
     * @return
     */
//    @Bean
    public ExecutionContextSerializer serializer() {
        return new Jackson2ExecutionContextStringSerializer();
    }

//    @Bean
//    @Autowired
    public JobRepository jobRepository( DataSource dataSource,
                                        PlatformTransactionManager txManager ) throws Exception
    {
        JobRepositoryFactoryBean fac = new JobRepositoryFactoryBean();
        fac.setDataSource( dataSource );
        fac.setTransactionManager( txManager );
        fac.setSerializer( serializer() );
        fac.afterPropertiesSet();
        return fac.getObject();
    }

//    @Bean
//    @Autowired
    public JobExplorer jobExplorer( DataSource dataSource ) throws Exception {
        JobExplorerFactoryBean fac = new JobExplorerFactoryBean();
        fac.setDataSource( dataSource );
        fac.setSerializer( serializer() );
        fac.afterPropertiesSet();
        return fac.getObject();
    }


}
