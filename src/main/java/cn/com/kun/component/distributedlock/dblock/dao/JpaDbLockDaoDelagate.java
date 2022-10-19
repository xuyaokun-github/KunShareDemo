package cn.com.kun.component.distributedlock.dblock.dao;

import cn.com.kun.component.distributedlock.dblock.entity.DbLockDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Component
public class JpaDbLockDaoDelagate implements DbLockDaoDelagate {

    @Autowired
    DblockJpaRepository dblockJpaRepository;

    @Override
    public DbLockDO acquireLock(Map<String, String> param) {

        return dblockJpaRepository.acquireLock(param);
    }

    @Override
    public DbLockDO selectLock(Map<String, String> param) {

        return dblockJpaRepository.selectLock(param);
    }

    @Transactional
    @Override
    public int updateRequestInfo(DbLockDO dbLockDO) {

        return dblockJpaRepository.updateRequestInfo(dbLockDO);
    }

    @Transactional
    @Override
    public int resetRequestInfo(DbLockDO dbLockDO) {

        return dblockJpaRepository.resetRequestInfo(dbLockDO);
    }

}
