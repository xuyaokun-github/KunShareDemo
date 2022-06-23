package cn.com.kun.service.mybatis.impl;

import cn.com.kun.bean.entity.DeadLockDemoDO;
import cn.com.kun.mapper.DeadLockDemoMapper;
import cn.com.kun.service.mybatis.DeadLockDemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeadLockDemoServiceImpl implements DeadLockDemoService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DeadLockDemoServiceImpl.class);

    @Autowired
    private DeadLockDemoMapper deadLockDemoMapper;

//    @Transactional(rollbackFor = Exception.class,isolation = Isolation.REPEATABLE_READ) //用可重复读，不会有死锁
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.SERIALIZABLE) //用串行化，会出现死锁
    @Override
    public void insert(DeadLockDemoDO deadLockDemoDO) {

        //先select，后update就会出现死锁
        deadLockDemoMapper.select(deadLockDemoDO);
        deadLockDemoMapper.insert(deadLockDemoDO);
    }

    @Override
    public void deleteAll() {
        deadLockDemoMapper.deleteAll();
    }



}
