package cn.com.kun.component.distributedlock.dblock.dao;

import cn.com.kun.component.distributedlock.dblock.entity.DbLockDO;
import cn.com.kun.component.jdbc.CommonNoTxJdbcStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Component
public class CustomJdbcDbLockDaoDelagate implements DbLockDaoDelagate {

    private final static String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 引入我的自定义JDBC组件(事务交由spring tx控制)
     */
    @Autowired
    private CommonNoTxJdbcStore commonJdbcStore;

    /**
     *
     @Select("select a.id id," +
    "a.resource resource," +
    "a.description description, " +
    "a.request_id requestId, " +
    "a.request_time requestTime " +
    "from tbl_database_lock a force index(uiq_idx_resource) " +
    "WHERE a.resource = #{resource} " +
    "for update")
     */
    private final static String SQL_ACQUIRE_LOCK = "select a.id id," +
            "a.resource resource," +
            "a.description description, " +
            "a.request_id requestId, " +
            "a.request_time requestTime " +
            "from tbl_database_lock a force index(uiq_idx_resource) " +
            "WHERE a.resource = '%s' " +
            "for update";

    /**
     *
     @Select("select a.id id," +
     "a.resource resource," +
     "a.description description, " +
     "a.request_id requestId, " +
     "a.request_time requestTime " +
     "from tbl_database_lock a " +
     "WHERE a.resource = #{resource} and a.request_id = #{requestId}")
     */
    private final static String SQL_SELECT_LOCK = "select a.id id," +
            "a.resource resource," +
            "a.description description, " +
            "a.request_id requestId, " +
            "a.request_time requestTime " +
            "from tbl_database_lock a " +
            "WHERE a.resource = '%s' and a.request_id = '%s'";

    /**
     *
     @Update("update tbl_database_lock set request_id=#{requestId}, request_time=#{requestTime} " +
     "WHERE resource = #{resource} ")
     */
    private final static String SQL_UPDATE_REQUESTINFO = "update tbl_database_lock set request_id='%s', request_time='%s' " +
            "WHERE resource = '%s' ";

    /**
     *
     @Update("update tbl_database_lock set request_id='', request_time=#{requestTime} " +
     "WHERE resource = #{resource} and request_id = #{requestId}")
     */
    private final static String SQL_RESET_REQUESTINFO = "update tbl_database_lock set request_id='', request_time=null " +
            "WHERE resource = '%s' and request_id = '%s'";
    /**
     *
     *
     * @param param
     * @return
     */
    @Override
    public DbLockDO acquireLock(Map<String, String> param) {

        String targetSql = String.format(SQL_ACQUIRE_LOCK, param.get("resource"));
        return commonJdbcStore.select(targetSql, DbLockDO.class);
    }

    @Override
    public DbLockDO selectLock(Map<String, String> param) {

        String targetSql = String.format(SQL_SELECT_LOCK, param.get("resource"), param.get("requestId"));
        return commonJdbcStore.select(targetSql, DbLockDO.class);
    }

    @Override
    public int updateRequestInfo(DbLockDO dbLockDO) {

        String targetSql = String.format(SQL_UPDATE_REQUESTINFO, dbLockDO.getRequestId(),
                toStr(dbLockDO.getRequestTime(), PATTERN_YYYY_MM_DD_HH_MM_SS),
                dbLockDO.getResource());
        return commonJdbcStore.update(targetSql);
    }

    @Override
    public int resetRequestInfo(DbLockDO dbLockDO) {

        String targetSql = String.format(SQL_RESET_REQUESTINFO, dbLockDO.getResource(), dbLockDO.getRequestId());
        return commonJdbcStore.update(targetSql);
    }


    private String toStr(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
