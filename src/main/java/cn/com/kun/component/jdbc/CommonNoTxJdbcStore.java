package cn.com.kun.component.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 公共JDBC操作
 * Created by xuyaokun On 2022/9/8 10:24
 *
 * @desc:
 * 参考自org.quartz.impl.jdbcjobstore.StdJDBCDelegate
 */
public class CommonNoTxJdbcStore {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommonNoTxJdbcStore.class);

    /**
     * 传入的SQL是已经拼接好的SQL(这样会有SQL注入的风险)
     *
     * @param sql (注意SQL的value部分，特殊字符可能会在保存时被MySQL自动去掉，慎用。
     *            假如有特殊字符，建议用我写的PreparedStatementParamProvider)
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T select(String sql, Class<T> tClass) {

        return select(sql, tClass, null);
    }


    /**
     * 自由定义参数
     *
     * @param sql
     * @param tClass
     * @param <T>
     * @return
     */
    public <T> T select(String sql, Class<T> tClass, PreparedStatementParamProvider psParamProvider) {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        try {
            conn = CustomDBConnectionManager.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            if (psParamProvider != null){
                psParamProvider.initPreparedStatementParam(ps);
            }
            rs = ps.executeQuery();
            T res = null;
            if (rs.next()) {
                //只取一个结果集（假如返回多行记录，要注意，要将最新的放前面）
                res = toJavaObject(rs, tClass);
            }

            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            //归还连接
            cleanupConnection(conn);
        }

        return null;
    }


    public <T> List<T> selectList(String sql, Class<T> tClass)  {

        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection conn = null;
        List<T> list = new ArrayList<>();
        try {
            conn = CustomDBConnectionManager.getInstance().getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                list.add(toJavaObject(rs, tClass));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            //归还连接
            cleanupConnection(conn);
        }
        return list;
    }


    /**
     * 插入、更新、删除（都是执行同一个方法）
     * @param updateSql (注意SQL的value部分，特殊字符可能会在保存时被MySQL自动去掉，慎用。
     *      假如有特殊字符，建议用我写的PreparedStatementParamProvider)
     */
    public int update(String updateSql){

        PreparedStatement ps = null;
        Connection conn = null;

        try {
            conn = CustomDBConnectionManager.getInstance().getConnection();
            ps = conn.prepareStatement(updateSql);
            int res = ps.executeUpdate();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStatement(ps);
            cleanupConnection(conn);
        }
        return 0;
    }


    private <T> T toJavaObject(ResultSet resultSet, Class<T> requiredClass) throws Exception {

        T instance = requiredClass.getDeclaredConstructor().newInstance();
        Field[] declaredFields = requiredClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            String fieldName = declaredField.getName();
            Class<?> type = declaredField.getType();
            declaredField.set(instance, getFieldValue(resultSet, fieldName, type));
        }
        return instance;
    }

    private <A> A getFieldValue(ResultSet resultSet, String fieldName, Class<A> fieldclazz) throws Exception {
        A object = null;
        try {
            if (isExitColumn(resultSet, fieldName)) {
                //假如结果集中存在该列，则获取具体的值
                if (fieldclazz.isAssignableFrom(java.util.Date.class)){
                    Timestamp timestamp = (Timestamp) resultSet.getObject(fieldName, Timestamp.class);
                    if (timestamp != null){
                        object = (A) new java.util.Date(timestamp.getTime());
                    }
                }else {
                    object = (A) resultSet.getObject(fieldName, fieldclazz);
                }
            }
        } catch (Exception e) {
            //时间类型，处理有问题 TODO
            LOGGER.error("getFieldValue异常", e);
        }
        return object;
    }

    private boolean isExitColumn(ResultSet resultSet, String fieldName) {
        try {
            //下标是从1开始
            return resultSet.findColumn(fieldName) >= 1;
        } catch (SQLException e) {
            if (e.getMessage().contains("Column") && e.getMessage().contains("not found")) {
                //出现列Not found异常
                LOGGER.info("结果集中未包含列:{}", fieldName);
            } else {
                LOGGER.error("isExitColumn异常", e);
            }
            return false;
        }
    }

    protected static void closeResultSet(ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException ignore) {
            }
        }
    }

    protected static void closeStatement(Statement statement) {
        if (null != statement) {
            try {
                statement.close();
            } catch (SQLException ignore) {
            }
        }
    }

    protected void cleanupConnection(Connection conn) {
        if (conn != null) {
            // Wan't a Proxy, or was a Proxy, but wasn't ours.
            closeConnection(conn);
        }
    }

    protected void closeConnection(Connection con) {
        // Will work for transactional and non-transactional connections.
        DataSourceUtils.releaseConnection(con, CustomDataSourceHolder.getDataSource());
    }


}
