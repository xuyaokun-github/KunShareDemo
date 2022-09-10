package cn.com.kun.component.jdbc;

import javax.sql.DataSource;

public class CustomDataSourceHolder {

    private static DataSource dataSource;

    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 应用初始化时注册主数据源
     * @param dataSource
     */
    public static void setDataSource(DataSource dataSource) {
        CustomDataSourceHolder.dataSource = dataSource;
    }

}
