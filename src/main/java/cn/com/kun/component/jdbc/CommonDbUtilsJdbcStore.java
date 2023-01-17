package cn.com.kun.component.jdbc;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.sql.*;
import java.util.List;

/**
 * 公共JDBC操作（用db utils作为底层实现）
 * Created by xuyaokun On 2022/9/8 10:24
 *
 * @desc:
 */
public class CommonDbUtilsJdbcStore {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommonDbUtilsJdbcStore.class);

    /**
     * BeanHandler
     *
     * @param sql
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T select(String sql, Class<T> tClass) {

        T result = null;
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = CustomDBConnectionManager.getInstance().getConnection();
            //返回的值结果集命名必须和实体类的属性名一致（并不会自动转驼峰）
            result = runner.query(conn, sql, new BeanHandler<T>(tClass));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
          if (conn != null){
              closeConnection(conn);
          }
        }
        return result;
    }


    /**
     * 自由定义参数(支持预处理)
     *
     * @param sql
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T select(String sql, Class<T> tClass, Object... params) {

        T result = null;
        Connection conn = null;
        try {
            QueryRunner runner = new QueryRunner();
            conn = CustomDBConnectionManager.getInstance().getConnection();
            //返回的值结果集命名必须和实体类的属性名一致（并不会自动转驼峰）
            result = runner.query(conn, sql, new BeanHandler<T>(tClass), params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null){
                closeConnection(conn);
            }
        }
        return result;
    }


    public <T> List<T> selectList(String sql, Class<T> tClass)  {

        QueryRunner runner = new QueryRunner();
        List<T> results = null;
        Connection conn = null;
        try {
            conn = CustomDBConnectionManager.getInstance().getConnection();
            results = runner.query(conn, sql, new BeanListHandler<T>(tClass));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null){
                closeConnection(conn);
            }
        }
        return results;
    }


    /**
     *
     */
    public int update(String updateSql, Object... params){

        QueryRunner runner = new QueryRunner();
        Integer result = null;
        Connection conn = null;
        try {
            conn = CustomDBConnectionManager.getInstance().getConnection();
            result = runner.update(conn, updateSql, params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null){
                closeConnection(conn);
            }
        }
        LOGGER.info("受影响记录条数：{}", result);
        return result;
    }

    /**
     * 或者可以调用 org.apache.commons.dbutils.DbUtils#close(java.sql.Connection)，本质一样的，最终都是调用close方法
     * @param con
     */
    protected void closeConnection(Connection con) {
        // Will work for transactional and non-transactional connections.
        DataSourceUtils.releaseConnection(con, CustomDataSourceHolder.getDataSource());
    }

}
