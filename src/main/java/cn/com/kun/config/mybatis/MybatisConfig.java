package cn.com.kun.config.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

//@Configuration
public class MybatisConfig {

    @Autowired
    private DataSource dataSource;

    /**
     * transactionManager bean名起transactionManager会和batch框架的事务管理器同名，报错：
     * The bean 'transactionManager', defined in class path resource
     * [org/springframework/batch/core/configuration/annotation/SimpleBatchConfiguration.class], could not be registered.
     * A bean with that name has already been defined in class path resource [cn/com/kun/config/mybatis/MybatisConfig.class] and overriding is disabled.
     *
     *
     *
     * @return
     */
    @Primary
    @Bean
    //这个创建动作是多余的，springboot会自动创建
    public DataSourceTransactionManager dataSourceTransactionManager(){

        return new DataSourceTransactionManager(dataSource);
    }
}
