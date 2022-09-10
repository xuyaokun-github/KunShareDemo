package cn.com.kun.component.distributedlock.dblock.dao;

import cn.com.kun.component.distributedlock.dblock.entity.DbLockDO;

import java.util.Map;

public interface DbLockDaoDelagate {


    DbLockDO acquireLock(Map<String, String> param);


    DbLockDO selectLock(Map<String, String> param);


    int updateRequestInfo(DbLockDO dbLockDO);

    int resetRequestInfo(DbLockDO dbLockDO);
}
