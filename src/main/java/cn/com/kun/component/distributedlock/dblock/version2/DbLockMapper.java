package cn.com.kun.component.distributedlock.dblock.version2;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

/**
 * 和version1共用同一张表
 * tbl_pessimistic_lock
 */
@Mapper
public interface DbLockMapper {

    @Select("select a.id id," +
            "a.resource resource," +
            "a.description description, " +
            "a.request_id requestId, " +
            "a.request_time requestTime " +
            "from tbl_database_lock a force index(uiq_idx_resource) " +
            "WHERE a.resource = #{resource} " +
            "for update")
    DbLockDO acquireLock(Map<String, String> param);

    @Select("select a.id id," +
            "a.resource resource," +
            "a.description description, " +
            "a.request_id requestId, " +
            "a.request_time requestTime " +
            "from tbl_database_lock a " +
            "WHERE a.resource = #{resource} and a.request_id = #{requestId}")
    DbLockDO selectLock(Map<String, String> param);

    @Update("update tbl_database_lock set request_id=#{requestId}, request_time=#{requestTime} " +
            "WHERE resource = #{resource} ")
    int updateRequestInfo(DbLockDO dbLockDO);

    @Update("update tbl_database_lock set request_id='', request_time=#{requestTime} " +
            "WHERE resource = #{resource} and request_id = #{requestId}")
    int resetRequestInfo(DbLockDO dbLockDO);

}
