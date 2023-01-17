package cn.com.kun.component.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
//@ConditionalOnSingleCandidate(DataSource.class)
//@AutoConfigureAfter({ DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
public class CustomJdbcConfig {

    /**
     * 主数据源
     */
    @Autowired(required = false)
    private DataSource dataSource;

    @Bean
    public CommonJdbcStore commonJdbcStore(){

        //断言(有些依赖包，不一定要用到访问数据库功能，这里允许为空)
//        Assert.notNull(dataSource, "主数据源不能为空");

        CustomDataSourceHolder.setDataSource(dataSource);
        return new CommonJdbcStore();
    }

    @Bean
    public CommonNoTxJdbcStore commonNoTxJdbcStore(){

        //断言(有些依赖包，不一定要用到访问数据库功能，这里允许为空)
//        Assert.notNull(dataSource, "主数据源不能为空");

        CustomDataSourceHolder.setDataSource(dataSource);
        return new CommonNoTxJdbcStore();
    }


    /**
     * 推荐用这个，不要自己实现JDBC，容易出错
     *
     * @return
     */
    @Bean
    public CommonDbUtilsJdbcStore commonDbUtilsJdbcStore(){

        CustomDataSourceHolder.setDataSource(dataSource);
        return new CommonDbUtilsJdbcStore();
    }

}
