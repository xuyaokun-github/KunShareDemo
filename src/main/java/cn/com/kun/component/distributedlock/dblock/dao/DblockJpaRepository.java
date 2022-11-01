package cn.com.kun.component.distributedlock.dblock.dao;

import cn.com.kun.component.distributedlock.dblock.entity.DbLockDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

/**
 * author:xuyaokun_kzx
 * date:2022/10/18
 * desc:
*/
public interface DblockJpaRepository extends JpaRepository<DbLockDO, Long> {

    @Query(value = "select a.id id," +
            "a.resource resource," +
            "a.description description, " +
            "a.request_id request_id, " +
            "a.request_time request_time " +
            "from tbl_database_lock a force index(uiq_idx_resource) " +
            "WHERE a.resource = :#{#param.get('resource')} " +
            "for update",
            nativeQuery = true)
    DbLockDO acquireLock(@Param("param") Map<String, String> param);

    @Query(value = "select a.id id," +
            "a.resource resource," +
            "a.description description, " +
            "a.request_id request_id, " +
            "a.request_time request_time " +
            "from tbl_database_lock a " +
            "WHERE a.resource = :#{#param.get('resource')} and a.request_id = :#{#param.get('requestId')}",
            nativeQuery = true)
    DbLockDO selectLock(@Param("param") Map<String, String> param);

    @Modifying
    @Query(value = "update tbl_database_lock set request_id=:#{#dbLockDO.requestId}, request_time=:#{#dbLockDO.requestTime} " +
            "WHERE resource = :#{#dbLockDO.resource} ",
            nativeQuery = true)
    int updateRequestInfo(@Param("dbLockDO") DbLockDO dbLockDO);

    @Modifying
    @Query(value = "update tbl_database_lock set request_id='', request_time=:#{#dbLockDO.requestTime} " +
            "WHERE resource = :#{#dbLockDO.resource} and request_id = :#{#dbLockDO.requestId}",
            nativeQuery = true)
    int resetRequestInfo(@Param("dbLockDO") DbLockDO dbLockDO);

}