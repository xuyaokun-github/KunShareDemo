package cn.com.kun.springframework.springcloud.springcloudtask.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.task.configuration.DefaultTaskConfigurer;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.cloud.task.configuration.TaskConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@ConditionalOnProperty(prefix = "kunsharedemo.springcloud-task", name = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableTask
public class EnableTaskConfig {

    //nothing

    @Autowired
//    @Qualifier("quartzDataSource") //假如存在多数据源，就要指定具体的数据源作为task的使用数据源
    DataSource dataSource;

    //为了解决springcloudtask无法支持多数据源的问题，必须指定明确的数据源
    @Bean
    public TaskConfigurer configurer() {
        return new DefaultTaskConfigurer(dataSource);
    }
}
