package cn.com.kun.component.jdbc;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class CustomDBConnectionManager {

    private static CustomDBConnectionManager instance = new CustomDBConnectionManager();

    private CustomDBConnectionManager() {
    }


    public Connection getConnection() throws SQLException {

        DataSource dataSource = CustomDataSourceHolder.getDataSource();
        Assert.notNull(dataSource, "主数据源不能为空");
        return DataSourceUtils.doGetConnection(dataSource);
    }

    public static CustomDBConnectionManager getInstance() {
        return instance;
    }

}

