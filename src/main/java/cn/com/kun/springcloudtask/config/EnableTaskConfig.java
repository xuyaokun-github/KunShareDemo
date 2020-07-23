package cn.com.kun.springcloudtask.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(prefix = "kunsharedemo.springcloud-task", name = "enabled", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableTask
public class EnableTaskConfig {

    //nothing
}
