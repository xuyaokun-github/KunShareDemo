package cn.com.kun.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 单测数据库的数据源配置
 *
 * Created by xuyaokun On 2020/6/25 14:21
 * @desc:
 */
@Configuration
@ConditionalOnProperty(prefix = "kunsharedemo.dbunit.datasource", value = {"enabled"}, havingValue = "true", matchIfMissing = true)
public class DbTestDataSourceConfig {


    @Value("${spring.datasource.testDataSource.url}")
    private String dbUrl;

    @Value("${spring.datasource.testDataSource.username}")
    private String username;

    @Value("${spring.datasource.testDataSource.password}")
    private String password;

    @Value("${spring.datasource.testDataSource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.testDataSource.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.testDataSource.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.testDataSource.maxActive}")
    private int maxActive;

    @Value("${spring.datasource.testDataSource.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.testDataSource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.testDataSource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.testDataSource.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.testDataSource.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.testDataSource.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.testDataSource.testOnReturn}")
    private boolean testOnReturn;

    @Value("${spring.datasource.testDataSource.poolPreparedStatements}")
    private boolean poolPreparedStatements;

    @Value("${spring.datasource.testDataSource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;

    @Value("${spring.datasource.testDataSource.filters}")
    private String filters;

    @Value("{spring.datasource.testDataSource.connectionProperties}")
    private String connectionProperties;

    @Bean(name="testDataSource")
    public DataSource testDataSource() {

        DruidDataSource datasource = new DruidDataSource();

        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);

        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        datasource.setConnectionProperties(connectionProperties);

        return datasource;
    }
}

