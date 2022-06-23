package cn.com.kun.springframework.batch.deadlockdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 排查完deadlock将该类的bean定义注释掉
 * author:xuyaokun_kzx
 * date:2022/6/22
 * desc:
*/
//@Configuration //复现死锁问题，再打开
public class DeadLockDemoTransactionManagerConfig {

    @Autowired
    DataSource dataSource;

    protected PlatformTransactionManager createTransactionManager() {
        return new DataSourceTransactionManager(this.dataSource);
    }

    @Bean
    @Primary //不加这个启动会失败
    public PlatformTransactionManager deadlockDemoTransactionManager() {
        PlatformTransactionManager transactionManager = createTransactionManager();
        return transactionManager;
    }

}
