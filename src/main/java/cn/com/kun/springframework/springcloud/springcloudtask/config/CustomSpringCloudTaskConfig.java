package cn.com.kun.springframework.springcloud.springcloudtask.config;


import cn.com.kun.springframework.springcloud.springcloudtask.linerunner.HelloWorldCommandLineRunner;
import cn.com.kun.springframework.springcloud.springcloudtask.linerunner.SecondCommandLineRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomSpringCloudTaskConfig {

    @Bean
    public CommandLineRunner commandLineRunner() {
        return new HelloWorldCommandLineRunner();
    }

    @Bean
    public CommandLineRunner secondCommandLineRunner() {
        return new SecondCommandLineRunner();
    }

}
