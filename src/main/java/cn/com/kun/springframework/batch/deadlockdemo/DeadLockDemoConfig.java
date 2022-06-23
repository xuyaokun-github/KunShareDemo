package cn.com.kun.springframework.batch.deadlockdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

//@Configuration //复现死锁问题，再打开
public class DeadLockDemoConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    protected ICustomJobRepository createCustomJobRepository() throws Exception {
        CustomJobRepositoryFactoryBean factory = new CustomJobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

}
